package org.roboy.memory.util;

import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;

import java.util.logging.Logger;

public class Validator {
    final private static Logger LOGGER = Logger.getLogger(Validator.class.toString());

    public boolean validateCreate(MemoryNodeModel node) {
        if (!node.isLegal()) {
            LOGGER.severe("FAIL CREATE: Node is illegal!");
            return false;
        }

        if (node.getProperties() == null) { //error msg if there are no properties
            LOGGER.severe("FAIL CREATE: No properties");
            return false;
        }

        if (!node.getProperties().containsKey(Neo4jProperty.name)){ //error msg if there is no node name
            LOGGER.severe("FAIL CREATE: No name specified in properties: name required");
            return false;
        }

        if (node.getLabel() != null && !Neo4jLabel.contains(node.getLabel().type)) {
            LOGGER.severe("FAIL CREATE: Label '" + node.getLabel() + "' doesn't exist in the DB");
            return false;
        }

        return true;
    }

    public boolean validateUpdate(MemoryNodeModel node) {
        if (!node.isLegal()) {
            LOGGER.severe("FAIL UPDATE: Node is illegal!");
            return false;
        }

        if(node.getId() <= 0) {
            LOGGER.severe("FAIL UPDATE: No ID specified");
            return false;
        }

        if(node.getRelationships() != null) {
            for (Neo4jRelationship rel : node.getRelationships().keySet()) {
                if (!Neo4jRelationship.contains(rel.type)) {
                    LOGGER.severe("FAIL UPDATE: The relationship type '" + rel + "' doesn't exist in the DB");
                    return false;
                }
            }
        }

        return true;
    }

    public boolean validateRemove(MemoryNodeModel node) {
        if (!node.isLegal()) {
            LOGGER.severe("FAIL REMOVE: Node is illegal!");
            return false;
        }

        if(node.getId() <= 0) {
            LOGGER.severe("FAIL REMOVE: No ID specified");
            return false;
        }

        return true;
    }

    public boolean validateGet(MemoryNodeModel node) {
        if (!node.isLegal()) {
            LOGGER.severe("FAIL GET: Node is illegal!");
            return false;
        }

        return true;
    }
}
