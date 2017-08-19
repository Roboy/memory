package org.roboy.memory.models;

import java.util.Map;

public class Get {

    private String type;
    private int id;

    private Map<String, String> relations;
    private Map<String, String> properties;

    public int getId() {

        return id;
    }

    public String getType() {

        return type;
    }

    public Map<String, String> getRelations() {

        return relations;
    }

    public Map<String, String> getProperties() {

        return properties;
    }
}
