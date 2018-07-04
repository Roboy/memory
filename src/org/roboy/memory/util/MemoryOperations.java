package org.roboy.memory.util;


import com.google.gson.Gson;
import org.roboy.memory.models.*;

import static org.roboy.memory.util.Answer.error;
import static org.roboy.memory.util.Answer.ok;

/**
 * This class replicates the behaviour of ros.ServiceLogic. We make use of the exact same functions, just refactored to not be Services.
 */
public class MemoryOperations {

    private static Gson parser = new Gson();
    /**
     * Create a node
     * @param request Query with data regarding the node. Ex: {"labels":["Organization"],"label":"Organization","properties":{"name":"korn"}}
     * @return JSON containing the ID of the new node
     */
    public static String create (String request){
        Create create = parser.fromJson(request, Create.class);
        if (create.validate()) {
            if (!create.getLabel().toUpperCase().equals("OTHER")) {
                return (Neo4j.createNode(create));
            } else {
                return(create.getError());
            }
        } else {
            return(create.getError());
        }
    }

    /**
     * Get the Node ID
     * @param request Query to specify Node to get. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"}}
     * @return JSON containing ID of node
     */
    public static String get(String request){
        Get get = parser.fromJson(request, Get.class);

        if(get.getId() != null) {
            return Neo4j.getNodeById(get.getId());
        }
        else {
            if (!get.getLabel().toUpperCase().equals("OTHER")) {return Neo4j.getNode(get);}
        }

        return get.getError();
    }

    /**
     * Update Nodes
     * @param request Query to link two nodes together. Ex: {"labels":["Person"],"label":"Person","properties":{"name":"davis"},"relationships":{"FROM":[369]},"id":368}
     * @return JSON establishing whether or not the connection was made or not
     */
    public static String update(String request) {
        Update update = parser.fromJson(request, Update.class);

        if (update.validate()) {
            if (!update.getLabel().toUpperCase().equals("OTHER")) {
                return(ok(Neo4j.updateNode(update)));
            } else {
                return(error(update.getError()));
            }
        } else {
            return(error(update.getError()));
        }
    }

    /**
     * Cypher Method that is never called
     * TODO: Implement this feature or refactor it out, it's kind of here because there was a service
     * @param request
     * @return
     */
    public static String cypher(String request){
            return (Neo4j.run(request));
    }

    /**
     * @deprecated It appears that this method has been removed from ServiceLogic. This method should contain the code of remove, to make things consistent.
     * See SDE-60
     */
    @Deprecated
    public static String delete(String request){
        return (Neo4j.run(request));
    }


    /**
     * Delete a Node
     * @param request JSON query to delete a specified node. Ex: {'type':'node','id':361,'properties_list': ['sex'], 'relationships':{'FRIEND_OF':[426]}}
     * @return Whether or not deleting was successful or not
     */
    public static String remove(String request){
        Remove remove = parser.fromJson(request, Remove.class);

        if (remove.validate()) {
            return(ok(Neo4j.remove(remove)));
        } else {
            return(error(remove.getError()));
        }
    }
}
