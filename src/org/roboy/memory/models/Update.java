package org.roboy.memory.models;

import org.roboy.memory.util.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class Update extends RosNode {


    @Override
    public boolean validate() {

        if(getId() == null) {
            error("No ID specified");
            return false;
        }

        if(getRelationships() != null) {
            for (String rel : getRelationships().keySet()) {
                if (!Dictionary.RELATION_VALUES.contains(rel.toUpperCase())) {
                    error("The relationship type '" + rel + "' doesn't exist in the DB");
                    return false;
                }
            }
        }

        return true;
    }
}
