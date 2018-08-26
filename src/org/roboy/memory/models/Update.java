package org.roboy.memory.models;

import org.roboy.memory.util.Dictionary;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Update queries.
 */
@Deprecated
public class Update extends RosNode {


    @Override
    public boolean validate() {

        if(getId() == null) {
            error("FAIL: No ID specified");
            return false;
        }

        if(getRelationships() != null) {
            for (String rel : getRelationships().keySet()) {
                if (!Dictionary.RELATIONSHIP_VALUES.contains(rel.toUpperCase())) {
                    error("FAIL: The relationship type '" + rel + "' doesn't exist in the DB");
                    return false;
                }
            }
        }

        return true;
    }
}
