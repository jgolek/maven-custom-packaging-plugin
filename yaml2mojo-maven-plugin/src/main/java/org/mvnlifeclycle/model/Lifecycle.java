package org.mvnlifeclycle.model;

import java.util.List;

public class Lifecycle {

    public String packaging;

    public MojoModel standaloneMojo;

    public List<Phase> phases;
    
    public String getPackaging() {
        return packaging;
    }
    
    public List<Phase> getPhases() {
        return phases;
    }

}
