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
 * @goal my-test-process
 * @requiresDependencyResolution test
 * 
 */
public class MyTestProcess
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
        getLog().info("--  package-testa  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("org.codehaus.mojo"),
                        artifactId("exec-maven-plugin"),
                        version("1.2.1")
                ),
                goal("exec"),
                configuration(
                    element(name ("executable"),
                            value("echo")        
                    ), 
                    element(name ("commandlineArgs"),
                            value("package: Das ist ein Test1")        
                    )
                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                )
        );
        
        getLog().info(" ");
        getLog().info("--  step0 with ant  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-antrun-plugin"),
                        version("1.7")
                ),
                goal("run"),
                configuration(
                    element(name ("target"),
                        element(name ("echo"),
                                 attributes(
                                    attribute("message1", "Das ist ein Test"),
                                    attribute("message2", "Das ist ein Test"))        
                            ),
                            element(name ("echo"),
                                     attributes(
                                        attribute("message", "Das ist ein Test"))        
                                )          
                            )
                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                )
        );
        
        getLog().info(" ");
        getLog().info("--  step1  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("mygroupid"),
                        artifactId("myartifactid"),
                        version("myversion")
                ),
                goal("mygoal"),
                configuration(

                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                )
        );
        
        getLog().info(" ");
        getLog().info("--  step2  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("mygroupid"),
                        artifactId("myartifactid"),
                        version("myversion")
                ),
                goal("mygoal"),
                configuration(

                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                )
        );
        
        getLog().info(" ");
        getLog().info("--  step3 with config  ----------------------");
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
        
        getLog().info(" ");
        getLog().info("--  step4 with config  ----------------------");
        getLog().info(" ");
        executeMojo(
                plugin(
                        groupId("mygroupid"),
                        artifactId("myartifactid"),
                        version("myversion")
                ),
                goal("mygoal"),
                configuration(
                    element(name ("parameters"),
                        element(name ("parameter"),
                                value("1")        
                        ),
                        element(name ("parameter"),
                                value("2")        
                        ),
                        element(name ("parameter"),
                            element(name ("test1"),
                                    value("1")        
                            ),
                            element(name ("test2"),
                                    value("2")        
                            ),
                            element(name ("test3"),
                                element(name ("tests"),
                                    element(name ("test"),
                                            value("1")        
                                    ),
                                    element(name ("test"),
                                            value("2")        
                                    )          
                                )          
                            )          
                        )          
                    ), 
                    element(name ("parameters2"),
                        element(name ("parameter"),
                                value("1")        
                        ),
                        element(name ("parameter"),
                                value("2")        
                        ),
                        element(name ("parameter"),
                            element(name ("test1"),
                                    value("1")        
                            ),
                            element(name ("test2"),
                                    value("2")        
                            ),
                            element(name ("test3"),
                                element(name ("tests"),
                                    element(name ("test"),
                                            value("1")        
                                    ),
                                    element(name ("test"),
                                            value("2/3")        
                                    )          
                                )          
                            )          
                        )          
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






