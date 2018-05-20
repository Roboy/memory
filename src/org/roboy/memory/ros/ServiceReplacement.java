package org.roboy.memory.ros;


import com.google.gson.Gson;
import org.roboy.memory.models.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.DataQueryRequest;
import roboy_communication_cognition.DataQueryResponse;

import static org.roboy.memory.util.Answer.error;
import static org.roboy.memory.util.Answer.ok;

public class ServiceReplacement {

    private static Gson parser = new Gson();


    public static String createServiceHandler (DataQueryRequest request){
        Header header = parser.fromJson(request.getHeader(),  Header.class);
        Create create = parser.fromJson(request.getPayload(), Create.class);
        if (create.validate()) {
            if (create.getLabel().equals( "OTHER")) {
                return (Neo4j.createNode(create));
            } else {
                return(create.getError());
            }
        } else {
            return(create.getError());
        }
    };
    public static String createServiceHandler (String request){
        Create create = parser.fromJson(request, Create.class);
        if (create.validate()) {
            if (create.getLabel().equals( "OTHER")) {
                return (Neo4j.createNode(create));
            } else {
                return(create.getError());
            }
        } else {
            return(create.getError());
        }
    };




    public static String getServiceHandler(DataQueryRequest req){
        Header header = parser.fromJson(req.getHeader(), Header.class);
        System.out.println(req.getPayload());
        Get get = parser.fromJson(req.getPayload(), Get.class);

        if(get.getId()!=null)
            return Neo4j.getNodeById(get.getId());
        else
//            if (! get.getLabel().equals("OTHER") )
            if(get.getLabel() != "OTHER")

                return Neo4j.getNode(get);

        return get.getError();
    }

    public static String getServiceHandler(String json){
        Get get = parser.fromJson(json, Get.class);

        if(get.getId()!=null)
            return Neo4j.getNodeById(get.getId());
        else
//            if (! get.getLabel().equals("OTHER") )
            if(get.getLabel() != "OTHER")

                return Neo4j.getNode(get);

        return get.getError();
    }

    public static String updateServiceHandler(DataQueryRequest request) {
        Header header = parser.fromJson(request.getHeader(),  Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);

        if (update.validate()) {
            if (update.getLabel() != "OTHER") {
                return(ok(Neo4j.updateNode(update)));
            } else {
                return(error(update.getError()));
            }
        } else {
            return(error(update.getError()));
        }
    };

    public static String updateServiceHandler(String request) {
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
    };

    public static String cyperServiceHandler(DataQueryRequest request) {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        return (Neo4j.run(request.getPayload()));
    }
        public static String cyperServiceHandler(String request){
            return (Neo4j.run(request));
    }

    public static String deleteServiceHandler(DataQueryRequest request){
        Header header = parser.fromJson(request.getHeader(), Header.class);
        return (Neo4j.run(request.getPayload()));
    }
    public static String deleteServiceHandler(String request){
        return (Neo4j.run(request));
    }
    public static String removeServiceHandler(DataQueryRequest request){
        Header header = parser.fromJson(request.getHeader(),  Header.class);
        Remove remove = parser.fromJson(request.getPayload(), Remove.class);

        if (remove.validate()) {
            return(ok(Neo4j.remove(remove)));
        } else {
            return(error(remove.getError()));
        }
    };
    public static String removeServiceHandler(String request){
        Remove remove = parser.fromJson(request, Remove.class);

        if (remove.validate()) {
            return(ok(Neo4j.remove(remove)));
        } else {
            return(error(remove.getError()));
        }
    };
}
