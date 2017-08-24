package org.roboy.memory.models;

import org.roboy.memory.util.Dictionary;

import java.util.Map;

public class Update {

    private Integer id;
    private String type;
    private String label;

    private Map<String, String[]> relations;
    private Map<String, String> properties;

    private transient String error;

    public Integer getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, String[]> getRelations() {
        return relations;
    }

    private void error(String text) {
        error = text;
    }

    public boolean validate() {

        if(getId() == null) {
            error("No ID specified");
            return false;
        }

        if(getRelations() != null) {
            for (String rel : getRelations().keySet()) {
                if (!Dictionary.RELATION_VALUES.contains(rel.toUpperCase())) {
                    error("The relationship type '" + rel + "' doesn't exist in the DB");
                    return false;
                }
            }
        }

        return true;
    }

    public String getError() {
        return error;
    }
}
