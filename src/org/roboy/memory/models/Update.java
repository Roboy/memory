package org.roboy.memory.models;

import java.util.Map;

public class Update {

    private int id;
    private String type;
    private String label;

    private Map<String, String> relations;
    private Map<String, String> properties;

    public int getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, String> getRelations() {
        return relations;
    }
}
