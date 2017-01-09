package org.mvnlifeclycle.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

public class VelocityWriter {

	private Template template;
	
	public VelocityWriter(String templateName){
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		this.template = velocityEngine.getTemplate( templateName );
	}
	
	public void write(Object model, File pathWithFile) throws IOException{

    	Context context = new VelocityContext();
    	context.put("model", model);
    	
		if(!pathWithFile.exists()){
			pathWithFile.getParentFile().mkdirs();
		}else{
			pathWithFile.delete();
		}
		
		Writer writer = new FileWriter(pathWithFile);
	    this.template.merge(context , writer);
		writer.close();
	}
}
