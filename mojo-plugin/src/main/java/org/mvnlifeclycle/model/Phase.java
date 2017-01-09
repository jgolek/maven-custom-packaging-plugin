package org.mvnlifeclycle.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Phase {
	
	public String name;

	public List<MojoModel> mojos;

	public String getName() {
		return name;
	}
	
	public String getQualifiedMojoNames() {
		List<String> qualifiedNames = new ArrayList<String>();
		for(MojoModel model: mojos){
			qualifiedNames.add(model.qualifiedGoal);
		}
		return StringUtils.join(qualifiedNames, ",");
	}

	public List<MojoModel> getMojos() {
		return mojos;
	}

}
