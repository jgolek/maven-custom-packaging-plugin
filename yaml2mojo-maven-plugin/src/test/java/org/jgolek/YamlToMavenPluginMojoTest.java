package org.jgolek;

import java.io.File;

import org.jgolek.YamlToMavenPluginMojo;
import org.junit.Test;

public class YamlToMavenPluginMojoTest extends YamlToMavenPluginMojo {

    @Test
    public void testExecute() throws Exception {
        
        this.packagingFileDirectory = new File("src/test/resources/build/packaging");
        this.outputDirectory = new File("target/test");
        this.buildOutputDirectory = this.outputDirectory;

        // this.
        this.execute();
    }
}
