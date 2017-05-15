package org.jgolek.model;

import java.util.List;

public class Process {

    public String packaging;

    public MojoModel standaloneMojo;

    public List<Phase> phases;

    public String getPackaging() {
        return packaging;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public MojoModel getStandaloneMojo() {
        return standaloneMojo;
    }
}
