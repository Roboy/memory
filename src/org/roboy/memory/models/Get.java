package org.roboy.memory.models;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Get queries.
 */
@Deprecated
public class Get extends RosNode {

    @Override
    public boolean validate() {
        return true;
    }
}
