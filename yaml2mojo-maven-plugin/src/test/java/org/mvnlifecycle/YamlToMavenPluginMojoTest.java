package org.mvnlifecycle;

import java.io.File;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;
import org.mvnlifeclycle.YamlToMavenPluginMojo;

public class YamlToMavenPluginMojoTest extends YamlToMavenPluginMojo {

    @Test
    public void testExecute() throws Exception {
        
        this.packagingFileDirectory = new File("src/test/resources/build/packaging");
        this.outputDirectory = new File("target/test");
        this.buildOutputDirectory = this.outputDirectory;
        
        Logger logger = new ConsoleLogger();
        Log log = new DefaultLog(logger);
        this.setLog(log);

        // this.
        this.execute();
    }
}
