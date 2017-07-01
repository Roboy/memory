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
        Create create = parser.fromJson(request.getPayload(), Create.class);

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
}
