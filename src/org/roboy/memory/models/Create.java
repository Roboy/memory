package org.roboy.memory.models;

import java.util.Map;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Create queries.
 */
public class Create {
    private String type; ///< Currently only used to specify the type "node"
    private String label; ///<  Specifies the type of node that shall be created, like "Person"
    private String[] faceVector; ///< JSON array containing facial features from vision module

    private Map<String, String> properties; ///< Contains the node properties

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
