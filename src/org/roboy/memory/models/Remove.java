package org.roboy.memory.models;

import java.util.HashSet;

public class Remove extends RosNode {

    private HashSet<String> properties_list;

    public HashSet<String> getPropertiesList() {
        return properties_list;
    }

    @Override
    public boolean validate() {
        if(getId() == null) {
            error("No ID specified");
            return false;
        }
         return true;
    }
}
