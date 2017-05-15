package org.jgolek.model;

import java.util.ArrayList;
import java.util.List;

public class MojoModel {

    public String name;

    public String packageName;

    public String className;

    public String goal;

    public String qualifiedGoal;

    public final List<Plugin> plugins = new ArrayList<Plugin>();

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getGoal() {
        return goal;
    }

    public String getQualifiedGoal() {
        return qualifiedGoal;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}
