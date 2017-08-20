package org.roboy.memory.ros;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.roboy.memory.models.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;
import static org.roboy.memory.util.Answer.*;

class ServiceLogic {

    private static Gson parser = new Gson();

    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        System.out.println("payload: " + request.getPayload());
        Create create = parser.fromJson(request.getPayload(), Create.class);
<<<<<<< HEAD
        System.out.println("create: " + create.getLabel());
=======
        // {'type':'node','label':'Person','properties':{'name':'test3','surname':'test3'}}
>>>>>>> wagram-develop

        switch (create.getType()) {
            case "node": {
                Neo4j.createNode(create.getLabel(), create.getProperties());
                break;
            }
        }

        response.setAnswer(ok());
    };

    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);
        response.setAnswer(error("feel free to implement me"));
    };

    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> getServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class); // {"user":"userName","datetime":"timestamp"}
        Get get = parser.fromJson(request.getPayload(), Get.class);
        // {"label":"someLabel","id":someID, "relations":{"type": "friend_of", "id": 000000},
        // "properties":{"name":"someName","surname":"someSurname"}}

        if (get.getId() != -1) {
            response.setAnswer(Neo4j.getNodeById(get.getId()));
        } else {
            response.setAnswer(Neo4j.getNode(get.getLabel(), get.getRelations(), get.getProperties()));
        }
    };
}
