package org.roboy.memory.util;


import com.google.gson.Gson;
import org.roboy.memory.models.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.DataQueryRequest;
import roboy_communication_cognition.DataQueryResponse;

import static org.roboy.memory.util.Answer.error;
import static org.roboy.memory.util.Answer.ok;

public class MemoryOperations {

    private static Gson parser = new Gson();


    public static String create (String request){
        Create create = parser.fromJson(request, Create.class);
        if (create.validate()) {
            if (!create.getLabel().equals( "OTHER")) {
                return (Neo4j.createNode(create));
            } else {
                return(create.getError());
            }
        } else {
            return(create.getError());
        }
    }


    public static String get(String json){
        Get get = parser.fromJson(json, Get.class);

        if(get.getId()!=null)
            return Neo4j.getNodeById(get.getId());
        else
//            if (! get.getLabel().equals("OTHER") )
            if(get.getLabel() != "OTHER")

                return Neo4j.getNode(get);

        return get.getError();
    }

    public static String update(String request) {
        Update update = parser.fromJson(request, Update.class);

        if (update.validate()) {
            if (update.getLabel() != "OTHER") {
                return(ok(Neo4j.updateNode(update)));
            } else {
                return(error(update.getError()));
            }
        } else {
            return(error(update.getError()));
        }
    }

    public static String cypher(String request){
            return (Neo4j.run(request));
    }

    /**
     * @deprecated It appears that this method has been removed from ServiceLogic. This method should contain the code of remove, to make things consistent.
     * SDE-60
     */
    @Deprecated
    public static String delete(String request){
        return (Neo4j.run(request));
    }

    public static String remove(String request){
        Remove remove = parser.fromJson(request, Remove.class);

        if (remove.validate()) {
            return(ok(Neo4j.remove(remove)));
        } else {
            return(error(remove.getError()));
        }
    }
}
