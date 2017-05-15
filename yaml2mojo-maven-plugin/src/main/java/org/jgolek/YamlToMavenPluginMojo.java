package org.jgolek;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jgolek.model.Lifecycle;
import org.jgolek.utils.ComponentXmlWriter;
import org.jgolek.utils.MavenPluginWriter;
import org.jgolek.utils.YamlMojoReader;
import org.jgolek.utils.YamlUtils;

import com.google.common.collect.Lists;


@Mojo( name = "yaml2mojo" )
public class YamlToMavenPluginMojo extends AbstractMojo {
    
    @Parameter( defaultValue="src/main/build/packaging" )
    protected File packagingFileDirectory;

    @Parameter( defaultValue="src/main/build/goal" )
    protected File goalFileDirectory;

    @Parameter( defaultValue="${project.build.directory}/generated-sources" )
    protected File outputDirectory;

    @Parameter( defaultValue="${project.build.outputDirectory}" )
    protected File buildOutputDirectory;

    @Parameter( defaultValue="${project.groupId}" )
    protected String mojoGroupId;

    @Parameter( defaultValue="${project.artifactId}" )
    protected String mojoArtifactId;
    

    public void execute() throws MojoExecutionException {
        Log logger = getLog();
        YamlMojoReader     yamlReader         = new YamlMojoReader(this.mojoGroupId, this.mojoArtifactId);
        MavenPluginWriter  mavenPluginWriter  = new MavenPluginWriter(this.mojoGroupId, this.outputDirectory);
        ComponentXmlWriter componentXmlWriter = new ComponentXmlWriter(this.buildOutputDirectory);

        try {
            List<Lifecycle> lifecycleModels = Lists.newArrayList();

            Collection<File> yamlFiles = Lists.newArrayList();
            yamlFiles.addAll(YamlUtils.listYamlFiles(this.goalFileDirectory));
            yamlFiles.addAll(YamlUtils.listYamlFiles(this.packagingFileDirectory));

            for (File yamlFile : yamlFiles) {
                logger.info("Found yaml file: " + yamlFile.getAbsolutePath());

                Lifecycle lifecycleModel = yamlReader.readFromFile(yamlFile);
                lifecycleModels.add(lifecycleModel);

                mavenPluginWriter.writeToJava(lifecycleModel);
            }

            if (lifecycleModels.isEmpty()) {
                logger.warn("couldn't find yaml files");
            }

            componentXmlWriter.write(lifecycleModels);

        } catch (Exception exc) {
            throw new MojoExecutionException(exc.getMessage(), exc);
        }
    }
}
