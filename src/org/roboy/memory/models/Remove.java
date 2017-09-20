package org.roboy.memory.models;

import java.util.HashSet;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Remove queries.
 */
public class Remove extends RosNode {

    private HashSet<String> properties;

    public HashSet<String> getPropertiesList() {
        return properties;
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
