package org.mvnlifeclycle.utils;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.mvnlifeclycle.model.MojoModel;
import org.mvnlifeclycle.model.Phase;
import org.mvnlifeclycle.model.Lifecycle;

public class MavenPluginWriter {

    private static final String MOJO_TEMPLATE_NAME = "mojo.vm";

    private String mojoGroupId;

    private File outputDirectory;

    public MavenPluginWriter(String mojoGroupId, File outputDirectory) {
        super();
        this.mojoGroupId = mojoGroupId;
        this.outputDirectory = outputDirectory;
    }

    public void writeToJava(Lifecycle lifecycle) throws Exception {
        VelocityWriter templateWriter = new VelocityWriter(MOJO_TEMPLATE_NAME);

        if (lifecycle.standaloneMojo != null) {
            File pathWithFile = new File(this.outputDirectory, convertToFilePath(this.mojoGroupId,
                    lifecycle.standaloneMojo.className));
            templateWriter.write(lifecycle.standaloneMojo, pathWithFile);
        } else {
            for (Phase phase : lifecycle.phases) {
                for (MojoModel mojo : phase.mojos) {
                    File pathWithFile = new File(this.outputDirectory, convertToFilePath(this.mojoGroupId,
                            mojo.className));
                    templateWriter.write(mojo, pathWithFile);
                }
            }
        }
    }

    private String convertToFilePath(String packageName2, String className) {
        return StringUtils.replace(packageName2, ".", "/") + "/" + className + ".java";
    }
}
