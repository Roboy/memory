package org.roboy.memory.models;

import java.util.Map;

public class Remove {

    private int id;
    private String type;
    private String label;

    private Map<String, String> relations;
    private String[] properties;

    public int getId() {
        return id;
    }

    public String[] getProperties() {
        return properties;
    }

    public Map<String, String> getRelations() {
        return relations;
    }
}
