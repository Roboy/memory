package org.roboy.memory.ros;

import com.google.gson.Gson;
import com.sun.tools.classfile.Opcode;
import org.roboy.memory.models.*;
import static org.roboy.memory.util.Config.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.roboy.memory.util.Answer.*;

class ServiceLogic {

    private static Gson parser = new Gson();
    private static HashSet<String> labels = new HashSet<String>(Arrays.asList(LABEL_VALUES));
    private static HashSet<String> relations = new HashSet<String>(Arrays.asList(RELATION_VALUES));


    //Create
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        System.out.println("payload: " + request.getPayload());
        Create create = parser.fromJson(request.getPayload(), Create.class);

        System.out.println("create: " + create.getLabel());
        if (create.getFace() != null) {
            System.out.println("FaceVector: " + create.getFace().toString());
        }

        // {'type':'node','label':'Person','properties':{'name':'test3','surname':'test3'}}

        if (create.getProperties() == null) { //error msg if there are no properties
            response.setAnswer(error("no properties"));
        } else if (!create.getProperties().containsKey("name")){ //error msg if there is no node name
            response.setAnswer(error("no name specified in properties : name required"));
        } else if (!labels.contains(create.getLabel())) {
            response.setAnswer(error("Label '" + create.getLabel() + "' doesn't exist in the DB"));
        } else {
            response.setAnswer(Neo4j.createNode(create.getLabel(), create.getFace(), create.getProperties()));
        }
    };

    //Update
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);
        for (String rel : update.getRelations().keySet()) {
            if (!relations.contains(rel)) {
                response.setAnswer(error("The relationship type '" + rel + "' doesn't exist in the DB"));
                return;
            }
        }

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
