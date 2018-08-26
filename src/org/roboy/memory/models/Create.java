package org.roboy.memory.models;

import org.roboy.memory.util.Dictionary;


/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Create queries.
 */
@Deprecated
public class Create extends RosNode {

    private String[] faceVector;

    public String[] getFace() {
        return faceVector;
    }

    @Override
    public boolean validate() {
        if (this.getProperties() == null) { //error msg if there are no properties
            error("FAIL: No properties");
            return false;
        }

        if (!this.getProperties().containsKey("name")){ //error msg if there is no node name
            error("FAIL: No name specified in properties : name required");
            return false;
        }

        if (this.getLabel() != null && !Dictionary.LABEL_VALUES.contains(this.getLabel())) {
            error("FAIL: Label '" + this.getLabel() + "' doesn't exist in the DB");
            return false;
        }

        return true;
    }
}
