package org.mvnlifecycle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mvnlifeclycle.model.MojoModel;
import org.mvnlifeclycle.model.Parameter;
import org.mvnlifeclycle.model.Phase;
import org.mvnlifeclycle.model.Plugin;
import org.mvnlifeclycle.model.Lifecycle;
import org.mvnlifeclycle.utils.YamlMojoReader;

public class YamlMojoReaderTest{
	
	@Test
	public void testReadFromString() throws Exception {
		       
		YamlMojoReader reader = new YamlMojoReader("example", "artifct");
		
		URL resource = YamlMojoReaderTest.class.getResource("test-process.yml");
		assertNotNull(resource);
		
		Lifecycle process = reader.readFromString(IOUtils.toString(YamlMojoReaderTest.class.getResourceAsStream("test-process.yml")));
	
		assertEquals("test-packaging", process.packaging); 
		
		hasPhases(process, "phase1", "phase2");
		
        Phase phase1 = process.phases.get(0);
        
        assertEquals(1, phase1.mojos.size());
        
        MojoModel mojoModel = phase1.mojos.get(0);
        assertEquals("step1", mojoModel.name);
        assertEquals("Step1", mojoModel.className);
        assertEquals("step1", mojoModel.goal);
        assertEquals("myartifactid", mojoModel.plugins.get(0).artifactId);
        assertEquals("mygroupid", mojoModel.plugins.get(0).groupId);
        assertEquals("myversion", mojoModel.plugins.get(0).version);
        assertEquals("mygoal", mojoModel.plugins.get(0).goal);
//        assertNotNull(mojoModel.plugins.get(0).configuration);
        
        Phase phase2 = process.phases.get(1);
        
        MojoModel mojoP2S1 = phase2.mojos.get(0);
        assertEquals("step1", mojoP2S1.name);
        assertEquals("Step1", mojoP2S1.className);
        assertEquals("step1", mojoP2S1.goal);
        assertEquals("myartifactid", mojoP2S1.plugins.get(0).artifactId);
        assertEquals("mygroupid", mojoP2S1.plugins.get(0).groupId);
        assertEquals("myversion", mojoP2S1.plugins.get(0).version);
        assertEquals("mygoal", mojoP2S1.plugins.get(0).goal);
//        assertNotNull(mojoP2S1.plugins.get(0).configuration);
        
        MojoModel mojoP2S2 = phase2.mojos.get(1);
        assertEquals("step2 with config", mojoP2S2.name);
        assertEquals("Step2WithConfig", mojoP2S2.className);
        assertEquals("step2-with-config", mojoP2S2.goal);
        assertEquals("myartifactid", mojoP2S2.plugins.get(0).artifactId);
        assertEquals("mygroupid", mojoP2S2.plugins.get(0).groupId);
        assertEquals("myversion", mojoP2S2.plugins.get(0).version);
        assertEquals("mygoal", mojoP2S2.plugins.get(0).goal);
        assertNotNull(mojoP2S2.plugins.get(0).configuration);
        
        List<Parameter> pluginConfiguration = mojoP2S2.plugins.get(0).configuration;
        assertEquals("parameter1", pluginConfiguration.get(0).name);
        assertEquals("value1",     pluginConfiguration.get(0).value);
        assertEquals("parameter2", pluginConfiguration.get(1).name);
        assertEquals("value2",     pluginConfiguration.get(1).value);
        
        MojoModel mojoP2S3 = phase2.mojos.get(2);
        List<Parameter> config2 = mojoP2S3.plugins.get(0).configuration;
        assertEquals("parameters", config2.get(0).name);
        assertEquals("parameter", config2.get(0).children.get(0).name);
        assertEquals("value1",    config2.get(0).children.get(0).value);
        assertEquals("parameter", config2.get(0).children.get(1).name);
        assertEquals("value2",    config2.get(0).children.get(1).value);
	}

	private void hasPhases(Lifecycle process, String... names) {
		assertTrue(!process.phases.isEmpty());
		
		int index = 0;
		for (String string : names) {
			Phase phase = process.phases.get(index);
			assertEquals(string, phase.name );
			index++;
		}
	}
	
	@Test
	public void testReadStandaloneString() throws Exception {
		       
		YamlMojoReader reader = new YamlMojoReader("example", "artifact");
		
		URL resource = YamlMojoReaderTest.class.getResource("test-process-standalone.yml");
		assertNotNull(resource);
		
		Lifecycle process = reader.readFromString(IOUtils.toString(YamlMojoReaderTest.class.getResourceAsStream("test-process-standalone.yml")));
	
		assertEquals("none", process.packaging);
		
        MojoModel mojo = process.standaloneMojo;
        
        assertEquals("my-test-process", mojo.goal);
        assertEquals("my-test-process", mojo.name);
        assertEquals("MyTestProcess", mojo.className);
        
        Plugin plugin1 = mojo.plugins.get(0);
        
        assertEquals("step1", plugin1.name);
        assertEquals("mygoal", plugin1.goal);
        assertEquals("myartifactid", plugin1.artifactId);
        assertEquals("mygroupid", plugin1.groupId);
        assertEquals("myversion", plugin1.version);
        
        Plugin plugin2 = mojo.plugins.get(1);
        
        assertEquals("step2", plugin2.name);
        assertEquals("mygoal", plugin2.goal);
        assertEquals("myartifactid", plugin2.artifactId);
        assertEquals("mygroupid", plugin2.groupId);
        assertEquals("myversion", plugin2.version);
        
        Plugin plugin3 = mojo.plugins.get(2);
        
        assertEquals("step3 with config", plugin3.name);
        assertEquals("mygoal", plugin3.goal);
        assertEquals("myartifactid", plugin3.artifactId);
        assertEquals("mygroupid", plugin3.groupId);
        assertEquals("myversion", plugin3.version);
        
        List<Parameter> pluginConfiguration = plugin3.configuration;
        assertEquals("parameter1", pluginConfiguration.get(0).name);
        assertEquals("value1",     pluginConfiguration.get(0).value);
        assertEquals("parameter2", pluginConfiguration.get(1).name);
        assertEquals("value2",     pluginConfiguration.get(1).value);
       
	}
	
	@Test
	public void testReadPackagingWithAnt() throws Exception {
		       
		YamlMojoReader reader = new YamlMojoReader("example", "artifact");
		
		URL resource = YamlMojoReaderTest.class.getResource("test-process-with-ant.yml");
		assertNotNull(resource);
		
		Lifecycle process = reader.readFromString(IOUtils.toString(YamlMojoReaderTest.class.getResourceAsStream("test-process-with-ant.yml")));
	
		assertEquals("test-packaging-with-ant", process.packaging);
		
        Phase phase = process.phases.get(0);
        
        MojoModel mojo = phase.mojos.get(0);
        
        assertEquals("step1", mojo.goal);
        assertEquals("step1", mojo.name);
        assertEquals("Step1", mojo.className);
        
        Plugin plugin1 = mojo.plugins.get(0);
        
        assertEquals("step1", plugin1.name);
        assertEquals("run", plugin1.goal);
        assertEquals("maven-antrun-plugin", plugin1.artifactId);
        assertEquals("org.apache.maven.plugins", plugin1.groupId);
        assertEquals("1.7", plugin1.version);
        
        List<Parameter> pluginConfiguration = plugin1.configuration;
        
        Parameter parameterTaget = pluginConfiguration.get(0);
        assertEquals("target", parameterTaget.name);
		Parameter parameterMove1 = parameterTaget.children.get(0);
        
        assertEquals("move", parameterMove1.name);
        assertEquals("inputFile1",     map(parameterMove1.attributes).get("file"));
        assertEquals("outputFile1",     map(parameterMove1.attributes).get("tofile"));
        
        Parameter parameterMove2 = parameterTaget.children.get(1);
        assertEquals("move", parameterMove2.name);
        assertEquals("inputFile2",     map(parameterMove2.attributes).get("file"));
        assertEquals("outputFile2",     map(parameterMove2.attributes).get("tofile"));
        
        assertEquals("include*",     parameterMove2.children.get(0).attributes.get("name"));
        assertEquals("exclude*",     parameterMove2.children.get(1).attributes.get("name"));
	}	
	
	public static Map<String, Object> map(Object obj){
		return (Map<String, Object>) obj;
	}
	
}
