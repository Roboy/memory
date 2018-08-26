package org.roboy.memory.interfaces;


import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.memory.util.NativeMemoryOperations;

/**
 * This Class creates an interface to connect to memory.
 * Instead of calling via a service via ROS, we simply call the function directly and get returned a JSON string.
 */
public interface Neo4jMemoryOperations {

    /**
     * Get the Node ID
     * @param query Query to specify Node to get. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"}}
     * @return JSON containing ID of node
     */
     static MemoryNodeModel get(MemoryNodeModel query) { return NativeMemoryOperations.get(query); }

    /**
     * Create a node
     * @param query Query with data regarding the node. Ex: {"labels":["Organization"],"label":"Organization","properties":{"name":"korn"}}
     * @return JSON containing the ID of the new node
     */
     static MemoryNodeModel create(MemoryNodeModel query) { return NativeMemoryOperations.create(query); }

    /**
     * Update Nodes
     * @param query Query to link two nodes together. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"},"relationships":{"FROM":[369]},"id":368}
     * @return JSON establishing whether or not the connection was made or not
     */
     static MemoryNodeModel update(MemoryNodeModel query) { return NativeMemoryOperations.update(query); }

    /**
     * Delete a Node
     * @param query JSON query to delete a specified node. Ex: {'type':'node','id':361,'properties_list': ['sex'], 'relationships':{'FRIEND_OF':[426]}}
     * @return Whether or not deleting was successful or not
     */
     static boolean remove(MemoryNodeModel query) { return NativeMemoryOperations.remove(query); }
}