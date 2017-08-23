package org.roboy.memory.models;

import java.util.Map;

public class Create {
    private String type;
    private String label;
    private String[] faceVector;

    private Map<String, String> properties;

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String[] getFace() {
        return faceVector;
    }
}
