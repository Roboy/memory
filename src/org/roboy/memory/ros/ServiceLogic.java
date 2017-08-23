package org.roboy.memory.ros;

import com.google.gson.Gson;
import org.roboy.memory.models.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;

import java.util.HashSet;

import static org.roboy.memory.util.Answer.*;

class ServiceLogic {

    private static Gson parser = new Gson();
    private static HashSet<String> labels = new HashSet<String>();

    //Create
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        System.out.println("payload: " + request.getPayload());
        Create create = parser.fromJson(request.getPayload(), Create.class);

        System.out.println("create: " + create.getLabel());
        System.out.println("FaceVector: " + create.getFace());

        // {'type':'node','label':'Person','properties':{'name':'test3','surname':'test3'}}

        if (create.getProperties() == null) { //error msg if there are no properties
            response.setAnswer(error("no properties"));
        } if (!create.getProperties().containsKey("name")){ //error msg if there is no node name
            response.setAnswer(error("no name specified in properties : name required"));
        } else {
            response.setAnswer(Neo4j.createNode(create.getLabel(), create.getFace(), create.getProperties()));
        }
    };

    //Update
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);

        Neo4j.updateNode(update.getId(), update.getRelations(), update.getProperties());

        response.setAnswer(ok());
    };

    //Get
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> getServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class); // {"user":"userName","datetime":"timestamp"}
        Get get = parser.fromJson(request.getPayload(), Get.class);
        // {"label":"someLabel","id":someID, "relations":{"type": "friend_of", "id": 000000},
        // "properties":{"name":"someName","surname":"someSurname"}}
        if (get.getId() != 0) {
            // {'id':someID}
            response.setAnswer(Neo4j.getNodeById(get.getId()));
        } else {
            response.setAnswer(Neo4j.getNode(get.getLabel(), get.getRelations(), get.getProperties()));
        }
    };

    //Cypher
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> cypherServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);

        response.setAnswer(Neo4j.run(request.getPayload()));
    };

    //Remove
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> removeServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Remove remove = parser.fromJson(request.getPayload(), Remove.class);

        Neo4j.remove(remove.getId(), remove.getRelations(), remove.getProperties());

        response.setAnswer(ok());
    };

}
