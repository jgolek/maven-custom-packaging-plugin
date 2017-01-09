package org.mvnlifeclycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parameter {

	public String name; 
	
	public Object value;
	
	public Map<String, String> attributes;
	
	public List<Parameter> children = new ArrayList<Parameter>();

	public Parameter() {
		super();
	}
	
	public Parameter(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
	
	public List<Parameter> getChildren() {
		return children;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public boolean hasChildren(){
		return !children.isEmpty();
	}
	
	public boolean hasAttributes(){
		if(attributes == null) return false;
		return !attributes.isEmpty();
	}
	
	
}
