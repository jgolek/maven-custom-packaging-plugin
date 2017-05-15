package org.mvnlifeclycle.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

public class ElementWithAttributes extends Element {

    private final Map<String, String> attributes;

    public ElementWithAttributes(String name, Map<String, String> attributes, Element[] elements) {
        super(name, elements);
        this.attributes = attributes;
    }

    public Xpp3Dom toDom() {
        Xpp3Dom dom = super.toDom();
        for (Entry<String, String> attribute : attributes.entrySet()) {
            dom.setAttribute(attribute.getKey(), attribute.getValue());
        }
        return dom;
    }

    public static Element element(String name, Map<String, String> attributes, Element... elements) {
        return new ElementWithAttributes(name, attributes, elements);
    }

    public static Element element(String name, Map<String, String> attributes) {
        return new ElementWithAttributes(name, attributes, new Element[0]);
    }

    public static Map<String, String> attributes(Entry<String, String>... attribute) {
        Map<String, String> map = new LinkedHashMap();
        for (Entry<String, String> entry : attribute) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static Entry<String, String> attribute(String key, String value) {
        return new DefaultMapEntry(key, value);
    }

    public static String value(String value) {
        return value;
    }

}
