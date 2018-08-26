package org.roboy.memory.util;

import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.ontology.Neo4jLabel;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class replicates the behaviour of ros.ServiceLogic. We make use of the exact same functions, just refactored to not be Services.
 */
public class NativeMemoryOperations {
    final private static Logger LOGGER = Logger.getLogger(NativeMemoryOperations.class.toString());
    private static Validator validator = new Validator();

    /**
     * Create a node
     * @param request Query with data regarding the node. Ex: {"labels":["Organization"],"label":"Organization","properties":{"name":"korn"}}
     * @return JSON containing the ID of the new node
     */
    public static Integer create(MemoryNodeModel request){
        if (validator.validateCreate(request)) {
            if (!request.getLabel().equals(Neo4jLabel.Other) &&
                    !request.getLabel().equals(Neo4jLabel.None)) {
                return (NativeNeo4j.createNode(request));
            }
        }

        LOGGER.severe("Error on creating: " + request.toString());
        return null;
    }

    /**
     * Get the Node ID
     * @param request Query to specify Node to get. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"}}
     * @return JSON containing ID of node
     */
    public static ArrayList<MemoryNodeModel> get(MemoryNodeModel request, Neo4jMemoryInterface memory){
        if (validator.validateGet(request)) {
            if (!request.getLabel().equals(Neo4jLabel.Other) &&
                    !request.getLabel().equals(Neo4jLabel.None)) {
                return NativeNeo4j.getNode(request, memory);
            }
        }

        LOGGER.severe("Error on getting: " + request.toString());
        return null;
    }

    /**
     * Get the Node ID
     * @param id Query to specify Node to get. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"}}
     * @return JSON containing ID of node
     */
    public static MemoryNodeModel getById(Integer id, Neo4jMemoryInterface memory){
        if (id != null && id > 0) {
            return NativeNeo4j.getNodeById(id, memory);
        }

        LOGGER.severe("Error on getting node by ID: " + id.toString());
        return null;
    }

    /**
     * Update Nodes
     * @param request Query to link two nodes together. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"},"relationships":{"FROM":[369]},"id":368}
     * @return JSON establishing whether or not the connection was made or not
     */
    public static boolean update(MemoryNodeModel request) {
        if (validator.validateUpdate(request)) {
            if (!request.getLabel().equals(Neo4jLabel.Other) &&
                    !request.getLabel().equals(Neo4jLabel.None)) {
                return NativeNeo4j.updateNode(request);
            }
        }

        LOGGER.severe("Error on updating: " + request.toString());
        return false;
    }

    /**
     * Delete a Node
     * @param request JSON query to delete a specified node. Ex: {'type':'node','id':361,'properties_list': ['sex'], 'relationships':{'FRIEND_OF':[426]}}
     * @return Whether or not deleting was successful or not
     */
    public static boolean remove(MemoryNodeModel request){
        if (validator.validateRemove(request)) {
            return NativeNeo4j.remove(request);
        }

        LOGGER.severe("Error on removing: " + request.toString());
        return false;
    }
}
