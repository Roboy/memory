package org.roboy.memory.ros;

import com.google.gson.Gson;
import org.roboy.memory.models.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.DataQueryRequest;
import roboy_communication_cognition.DataQueryResponse;

import static org.roboy.memory.util.Answer.error;
import static org.roboy.memory.util.Answer.ok;

/**
 * Contains service handlers to talk with ROS.
 * They parse the header and payload and check for invalid elements in the query.
 * Then the functions to construct the cypher queries are excecuted and the answer returned.
 */
class ServiceLogic {

    private static Gson parser = new Gson();

    /**
     * Create Service Handler.
     * Parses the header and payload into a create object with Gson and checks for invalid elements in the query.
     * Calls  createNode() method to query Neo4j and the answer is returned.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Create create = parser.fromJson(request.getPayload(), Create.class);
        if (create.validate()) {
            response.setAnswer(Neo4j.createNode(create));
        } else {
            response.setAnswer(create.getError());
        }
    };

    /**
     * Update Service Handler.
     * Parses the header and payload into an update object with Gson and checks for invalid relationship types in the query.
     * Calls updateNode() method to query Neo4j and the answer is returned.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);

        if (update.validate()) {
            response.setAnswer(ok(Neo4j.updateNode(update)));
        } else {
            response.setAnswer(error(update.getError()));
        }
    };

    /**
     * Get Service Handler.
     * Parses the header and payload into a get object with Gson and checks whether node IDs or information about a node is queried.
     * Calls getNodeById() or getNode() methods to query Neo4j and the answer is returned.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> getServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Get get = parser.fromJson(request.getPayload(), Get.class);
        if (get.getId() != null ) {
            response.setAnswer(Neo4j.getNodeById(get.getId()));
        } else {
            response.setAnswer(Neo4j.getNode(get));
        }
    };

    /**
     * Cypher Service Handler.
     * Directly runs a plain Cypher query which is contained in the payload and returns the response.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> cypherServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);

        response.setAnswer(Neo4j.run(request.getPayload()));
    };

    /**
     * Remove Service Handler.
     * Parses the header and payload into a remove object.
     * Calls remove() method to query Neo4j and the answer is returned.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> removeServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Remove remove = parser.fromJson(request.getPayload(), Remove.class);

        if (remove.validate()) {
            response.setAnswer(ok(Neo4j.remove(remove)));
        } else {
            response.setAnswer(error(remove.getError()));
        }
    };

}
