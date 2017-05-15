package org.mvnlifeclycle.utils;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class YamlUtils {

    public static Collection<File> listYamlFiles(File directory) {
        if (directory != null && directory.exists()) {
            return FileUtils.listFiles(directory, new String[] { "yml", "yaml" }, true);
        } else {
            return Lists.newArrayList();
        }
    }

}
