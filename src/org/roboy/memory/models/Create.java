package org.roboy.memory.models;

import org.apache.maven.shared.utils.StringUtils;
import org.roboy.memory.util.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class Create {
    @Deprecated
    private String type;

    private String label;
    private String[] faceVector;
    private transient String error;

    private HashMap<String, String> properties;

    public String getLabel() {
        return StringUtils.capitalise(label.toLowerCase());
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public String[] getFace() {
        return faceVector;
    }

    private void error(String text) {
        error = text;
    }

    public String getError() {
        return error;
    }

    public boolean validate() {
        if (this.getProperties() == null) { //error msg if there are no properties
            error("no properties");
            return false;
        }

        if (!this.getProperties().containsKey("name")){ //error msg if there is no node name
            error("no name specified in properties : name required");
            return false;
        }

        if (this.getLabel() != null && !Dictionary.LABEL_VALUES.contains(this.getLabel())) {
            error("Label '" + this.getLabel() + "' doesn't exist in the DB");
            return false;
        }

        return true;
    }
}
