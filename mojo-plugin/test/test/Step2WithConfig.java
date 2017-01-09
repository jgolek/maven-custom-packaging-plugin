package test;


import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import static org.mvnlifeclycle.model.ElementWithAttributes.attribute;
import static org.mvnlifeclycle.model.ElementWithAttributes.attributes;
import static org.mvnlifeclycle.model.ElementWithAttributes.element;
import static org.mvnlifeclycle.model.ElementWithAttributes.value;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;


/**
 *
 * @goal step2-with-config
 * @requiresDependencyResolution test
 * 
 */
public class Step2WithConfig
    extends AbstractMojo
{

  /**
   * The Maven Project Object
   *
   * @parameter expression="\${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;

  /**
   * The Maven Session Object
   *
   * @parameter expression="\${session}"
   * @required
   * @readonly
   */
  protected MavenSession session;

  /**
   * The Maven PluginManager Object
   *
   * @component
   * @required
   */
  protected BuildPluginManager pluginManager;
  

    public void execute()
        throws MojoExecutionException
    {
        getLog().info(" ");
        getLog().info("--  step2 with config  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("mygroupid"),
                        artifactId("myartifactid"),
                        version("myversion")
                ),
                goal("mygoal"),
                configuration(
                    element(name ("parameter1"),
                            value("value1")        
                    ), 
                    element(name ("parameter2"),
                            value("value2")        
                    )
                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                )
        );
        
    }
}






