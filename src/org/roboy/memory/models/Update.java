package org.roboy.memory.models;

import java.util.Map;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Update queries.
 */
public class Update {
    private int id; ///< The id of a node that shall be modified
    private String type;  ///< Currently only used to specify the type "node"
    private String label; ///<  Specifies the type of node that shall be updated, like "Person"

    private Map<String, String[]> relations; ///< Contains the relationship type as key and an array of node IDs as value
    private Map<String, String> properties; ///< Contains the node properties

    public int getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, String[]> getRelations() {
        return relations;
    }
}
