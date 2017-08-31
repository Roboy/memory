package org.roboy.memory.models;

import java.util.Map;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Get queries.
 */
public class Get {

    private String label; ///<  Specifies the type of node that shall be searched, like "Person"
    private int id; ///< The id of a node that shall be searched

    private Map<String, String[]> relations;  ///< Contains the relationship type as key and an array of node IDs as value
    private Map<String, String> properties; ///< Contains the node properties

    public int getId() {return id;}

    public String getLabel() {return label;}

    public Map<String, String[]> getRelations() {return relations;}

    public Map<String, String> getProperties() {return properties;}
}
