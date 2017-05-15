package org.mvnlifeclycle;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.mvnlifeclycle.model.Lifecycle;
import org.mvnlifeclycle.utils.ComponentXmlWriter;
import org.mvnlifeclycle.utils.MavenPluginWriter;
import org.mvnlifeclycle.utils.YamlMojoReader;
import org.mvnlifeclycle.utils.YamlUtils;

import com.google.inject.internal.util.Lists;

/**
 *
 * @goal yaml2mojo
 * 
 */
public class YamlToMavenPluginMojo extends AbstractMojo {
    /**
     * @parameter default-value="src/main/build/packaging"
     */
    protected File packagingFileDirectory;

    /**
     * @parameter default-value="src/main/build/goal"
     */
    protected File goalFileDirectory;

    /**
     * @parameter default-value="${project.build.directory}/generated-sources"
     */
    protected File outputDirectory;

    /**
     * @parameter default-value="${project.build.outputDirectory}"
     */
    protected File buildOutputDirectory;

    /**
     * @parameter default-value="org.mvnlifecycle"
     */
    protected String mojoGroupId;

    /**
     * @parameter default-value="${project.artifactId}"
     */
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
