package org.roboy.memory.ros;

import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;

class ServiceLogic {

    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createNodeServiceHandler = (request, response) -> {
        //logic here
        response.setAnswer("");
    };

    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createRelationServiceHandler = (request, response) -> {
        //logic here
        response.setAnswer("");
    };
}
