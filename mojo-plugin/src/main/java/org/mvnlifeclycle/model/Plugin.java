package org.mvnlifeclycle.model;

import java.util.List;
import java.util.Map;

public class Plugin {
	
	public String name;

	public String qualifiedGoal;
	
	public String groupId;
	
	public String artifactId;
	
	public String version;
	
	public String goal;
	
	public List<Parameter> configuration;

	public String getQualifiedGoal() {
		return qualifiedGoal;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	public String getGoal() {
		return goal;
	}

	public List<Parameter> getConfiguration() {
		return configuration;
	}
	
	public String getName() {
		return name;
	}
}
