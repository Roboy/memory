package org.roboy.memory.ros;

import com.google.gson.Gson;
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


    //Create
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Create create = parser.fromJson(request.getPayload(), Create.class);

        if(create.validate()) {
            response.setAnswer(Neo4j.createNode(create));
            return;
        }

        response.setAnswer(create.getError());
    };

    //Update
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);

        if(update.validate()) {
            response.setAnswer(ok(Neo4j.updateNode(update)));
            return;
        }

        response.setAnswer(error(update.getError()));
    };

    //Get
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> getServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Get get = parser.fromJson(request.getPayload(), Get.class);
        if (get.getId() != 0) {
            response.setAnswer(Neo4j.getNodeById(get.getId()));
        } else {
            response.setAnswer(Neo4j.getNode(get));
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

        if(remove.validate()) {
            response.setAnswer(ok(Neo4j.remove(remove)));
        } else {
            response.setAnswer(error(remove.getError()));
        }

    };

}
