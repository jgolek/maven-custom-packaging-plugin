package org.jgolek.utils;

import java.io.File;
import java.util.List;

import org.jgolek.model.Lifecycle;

public class ComponentXmlWriter {

    private static final String COMPONENTS_XML_FILE_NAME = "META-INF/plexus/components.xml";

    private static final String COMPONENTS_XML_TEMPLATE_NAME = "components.xml.vm";

    private File outputDirectory;

    public ComponentXmlWriter(File outputDirectory) {
        super();
        this.outputDirectory = outputDirectory;
    }

    public void write(List<Lifecycle> processes) throws Exception {
        if (processes.isEmpty()) {
            return;
        }

        VelocityWriter templateWriter = new VelocityWriter(COMPONENTS_XML_TEMPLATE_NAME);
        File pathWithFile = new File(this.outputDirectory, COMPONENTS_XML_FILE_NAME);
        templateWriter.write(processes, pathWithFile);
    }
}
