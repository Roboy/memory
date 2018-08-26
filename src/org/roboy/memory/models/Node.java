package org.roboy.memory.models;

import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;

@Deprecated
public class Node {

    private String label;
    private Integer id;
    private HashMap<String, String> properties;
    private HashMap<String, int[]> relationships;

    public Node() {
    }

    public String getLabel() {
        String r = "";
        try{
            r = StringUtils.capitalise(label.toLowerCase());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    public void setLabel(String label) {
        this.label = StringUtils.capitalise(label.toLowerCase());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public HashMap<String, int[]> getRelationships() {
        return relationships;
    }

    public void setRelationships(HashMap<String, int[]> relationships) {
        this.relationships = relationships;
    }
}
