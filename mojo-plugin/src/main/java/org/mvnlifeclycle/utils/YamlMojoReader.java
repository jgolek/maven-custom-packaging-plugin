package org.mvnlifeclycle.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.mvnlifeclycle.model.Lifecycle;
import org.mvnlifeclycle.model.MojoModel;
import org.mvnlifeclycle.model.Parameter;
import org.mvnlifeclycle.model.Phase;
import org.mvnlifeclycle.model.Plugin;
import org.yaml.snakeyaml.Yaml;

public class YamlMojoReader {
	
	private final String groupId;
	private final String artifactId;

	public YamlMojoReader(String groupId, String artifactId) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
	}

	private List<Phase> convertToPhases(Map<String, Object> data) {

		List<Phase> phases = new ArrayList<Phase>();
		
		for(String key: data.keySet()){
			
			List<Map<String,Object>> plugins = (List<Map<String, Object>>) data.get(key);
			
			Phase phase = new Phase();
			phase.name = key;
			phase.mojos = new ArrayList<MojoModel>();
			
			for(Map<String,Object> pluginMap: plugins){
				
				MojoModel model = new MojoModel();
				Plugin plugin = convertToMojo(pluginMap);
				model.plugins.add(plugin);
				
				model.className = WordUtils.capitalize(plugin.name.replace("-", " ")).replace(" ", "");
				model.goal = plugin.name.replace(" ", "-");
				model.packageName = groupId;
				if(plugin.configuration == null){
					model.qualifiedGoal = plugin.groupId +":"+plugin.artifactId+":"+plugin.goal;
				}else{
					model.qualifiedGoal = groupId +":"+artifactId+":"+model.goal;
				}
				model.name = plugin.name;
				
				phase.mojos.add(model);
			}
			phases.add(phase);
		}
		
		return phases;
	}
	
	private Plugin convertToPlugin(Map<String, Object> mojo) {
		Plugin plugin = new Plugin();
		
		String mojoName = (String) mojo.get("name");
		
		String fullGoal = (String) mojo.get("plugin");
		String[] fullGoalArray = StringUtils.split(fullGoal, ":");
		
		plugin.name = mojoName;
		plugin.goal = mojoName.replace(" ", "-");
		plugin.groupId = fullGoalArray[0];
		plugin.artifactId = fullGoalArray[1];
		plugin.version = fullGoalArray[2];
		plugin.goal = fullGoalArray[3];
		
		if(mojo.containsKey("plugin-config")) {
			Map<String,Object> config = (Map<String, Object>) mojo.get("plugin-config");
			plugin.configuration = convertToParameters(config);
		}
		return plugin;
	}
	
	private Plugin convertToAntPlugin(Map<String, Object> mojo) {
		Plugin plugin = new Plugin();
		
		String mojoName = (String) mojo.get("name");
		
		plugin.name = mojoName;
		plugin.goal = mojoName.replace(" ", "-");
		plugin.groupId = "org.apache.maven.plugins";
		plugin.artifactId = "maven-antrun-plugin";
		plugin.version = "1.7";
		plugin.goal = "run";
		
		List<Parameter> tasksAsParameter = new ArrayList<Parameter>();

		Object antConfigObject = mojo.get("ant");
		if(!(antConfigObject instanceof List)){
			throw new RuntimeException("ant config should be a list!");
		}
		
		List<Map> tasks = (List<Map>) antConfigObject;
		for(Map task: tasks){
			if(task.keySet().size() > 1){
				throw new RuntimeException("ant task should only have one parameter");
			}
			String taskName = (String) task.keySet().iterator().next();
			Map taskConfig = (Map) task.get(taskName);
			Parameter parameter = convertToParameterWithAttributes(taskName, taskConfig);
			tasksAsParameter.add(parameter);
		}
		
		Parameter targetParameter = new Parameter();
		targetParameter.name = "target";
		targetParameter.children = tasksAsParameter;
		
		plugin.configuration = Arrays.asList(targetParameter);
		return plugin;
	}

	private Parameter convertToParameterWithAttributes(String taskName, Map taskConfig) {
		Parameter parameter = new Parameter();

		parameter.name = taskName;
		
		Map<String, String> attributes = new HashMap<String, String>();
		for(Object configKey: taskConfig.keySet()){
			Object value = taskConfig.get(configKey);
			if(value instanceof Map){
					parameter.children.add(convertToParameterWithAttributes((String) configKey, (Map)value));
			}else{
				attributes.put(String.valueOf(configKey), String.valueOf(value));
			}
		}
		parameter.attributes = attributes;
		return parameter;
	}

	private Plugin convertToMojo(Map<String, Object> mojo) {
		
		if(mojo.containsKey("plugin")){
			return convertToPlugin(mojo);
		}
		
		if(mojo.containsKey("ant")){
			return convertToAntPlugin(mojo);
		}
		
		if(mojo.containsKey("exec")){
			return convertToExecPlugin(mojo);
		}
		
		throw new RuntimeException("unknown type in your mojo definition. In "+String.valueOf(mojo.get("name"))+ "expected keys 'plugin' or 'ant'. ");
	}
	
	private Plugin convertToExecPlugin(Map<String, Object> mojo) {
	  Plugin plugin = new Plugin();
	  String mojoName = (String) mojo.get("name");
	  String exec = (String) mojo.get("exec");
	  
	  String executable = StringUtils.substringBefore(exec, " ");
	  String commandlineArgs = StringUtils.substringAfter(exec, " ");
			
	  plugin.name = mojoName;
	  plugin.goal = mojoName.replace(" ", "-");
	  plugin.groupId = "org.codehaus.mojo";
	  plugin.artifactId = "exec-maven-plugin";
	  plugin.version = "1.2.1";
	  plugin.goal = "exec";
	 	
	  List<Parameter> parameters = new ArrayList<Parameter>();
	  parameters.add(new Parameter("executable",      executable));
	  parameters.add(new Parameter("commandlineArgs", commandlineArgs));
	  
	  plugin.configuration = parameters;

	  return plugin;
	}

	private List<Parameter> convertToParameters(Map<String, Object> parameterMap) {

		List<Parameter> parameters = new ArrayList<Parameter>();
		for (String parameterName : parameterMap.keySet()) {
			Object value = parameterMap.get(parameterName);
			
			Parameter parameter = new Parameter();
			parameter.name = parameterName;

			if(value instanceof List){
				for (Object subParameterObject : (List)value) {
					Map<String, Object> subParameterMap = (Map<String, Object>) subParameterObject;
					Parameter subParameter = convertToParameters(subParameterMap).get(0);
					parameter.children.add(subParameter);
				}
			} else if (value instanceof Map){
				parameter.children.addAll(convertToParameters((Map<String, Object>) value));
			} else {
				parameter.value = StringEscapeUtils.escapeJava(String.valueOf(value)).replace("\\/", "/");
			}
			parameters.add(parameter);
		}
		
		return parameters;
	}
	
	private List<Parameter> convertToParametersWithAttributes(Map<String, Object> parameterMap) {

		List<Parameter> parameters = new ArrayList<Parameter>();
		for (String parameterName : parameterMap.keySet()) {
			Object value = parameterMap.get(parameterName);
			
			Parameter parameter = new Parameter();
			parameter.name = parameterName;

			if(value instanceof List){
				for (Object subParameterObject : (List)value) {
					Map<String, Object> subParameterMap = (Map<String, Object>) subParameterObject;
					Parameter subParameter = convertToParametersWithAttributes(subParameterMap).get(0);
					parameter.children.add(subParameter);
				}
			} else if (value instanceof Map){
				parameter.children.addAll(convertToParametersWithAttributes((Map<String, Object>) value));
			} else {
				parameter.value = StringEscapeUtils.escapeJava(String.valueOf(value)).replace("\\/", "/");
			}
			parameters.add(parameter);
		}
		
		return parameters;
	}

	private MojoModel convertToStandaloneMojo(List<Map<String, Object>> data) {

		MojoModel model = new MojoModel();
		
		for (Map<String, Object> pluginMap : data) {
			Plugin plugin = convertToMojo(pluginMap);
			model.plugins.add(plugin);
		}
		
		return model;
	}
	
	public Lifecycle readFromString(String processFileContent) throws Exception {
		Yaml yaml = new Yaml();
		
		Lifecycle lifecycle = new Lifecycle();
		
		Iterable<Object> loadAll = yaml.loadAll(processFileContent);
		Map<String, String> packagingMap = (Map<String, String>) loadAll.iterator().next();
		lifecycle.packaging = packagingMap.get("packaging");
		
		if("none".equals(lifecycle.packaging)) {
			List mojos = (List) loadAll.iterator().next();
			lifecycle.standaloneMojo = convertToStandaloneMojo(mojos);
			lifecycle.standaloneMojo.name = packagingMap.get("name");
			lifecycle.standaloneMojo.className = WordUtils.capitalize(lifecycle.standaloneMojo.name.replace("-", " ")).replace(" ", "");
			lifecycle.standaloneMojo.goal = lifecycle.standaloneMojo.name.replace(" ", "-");
			lifecycle.standaloneMojo.packageName = groupId;
			lifecycle.standaloneMojo.qualifiedGoal = groupId +":"+artifactId+":"+lifecycle.standaloneMojo.goal;
		} else {
			Map<String, Object> phasesMap = (Map<String, Object>) loadAll.iterator().next();
			lifecycle.phases = convertToPhases(phasesMap);
		}
		
		return lifecycle;
	}

	public Lifecycle readFromFile(File processFileArg) throws Exception {
		return readFromString(FileUtils.readFileToString(processFileArg));
	}
}
