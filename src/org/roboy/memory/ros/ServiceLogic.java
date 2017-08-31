package org.roboy.memory.ros;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.roboy.memory.models.*;
import static org.roboy.memory.util.Config.*;
import org.roboy.memory.util.Neo4j;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

import static org.roboy.memory.util.Answer.*;

/**
 * Contains the service handlers to talk with ROS.
 */
class ServiceLogic {

    private static Logger logger = Logger.getLogger(ServiceLogic.class.toString()); ///< Logger
    private static Gson parser = new Gson(); ///< Parses the JSON elements of the header and payload
    private static HashSet<String> labels = new HashSet<String>(Arrays.asList(LABEL_VALUES)); ///< Contains available label types
    private static HashSet<String> relations = new HashSet<String>(Arrays.asList(RELATION_VALUES)); ///< Contains available relationship types


    /**
     * Create Service Handler.
     * Parses the header and payload into a create object with Gson and check for invalid elements in the query.
     * Calls method to query Neo4j
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> createServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Create create = parser.fromJson(request.getPayload(), Create.class);

        //Print facial features
        if (create.getFace() != null) {
            System.out.println("FaceVector: " + create.getFace().toString());
        }

        ///Check for invalid elements in the query
        if (create.getProperties() == null) { //error msg if there are no properties
            response.setAnswer(error("no properties"));
            return;
        } else if (!create.getProperties().containsKey("name")){ //error msg if there is no node name
            response.setAnswer(error("no name specified in properties : name required"));
            return;
        } else if (create.getLabel() != null && !labels.contains(create.getLabel().substring(0,1).toUpperCase() + create.getLabel().substring(1).toLowerCase())) {
            response.setAnswer(error("Label '" + create.getLabel() + "' doesn't exist in the DB"));
            return;
        } else {
            response.setAnswer(Neo4j.createNode(create.getLabel(), create.getFace(), create.getProperties()));
        }
        
    };

    /**
     * Update Service Handler.
     * Parses the header and payload into an update object with Gson.
     * Calls method to query Neo4j
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> updateServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Update update = parser.fromJson(request.getPayload(), Update.class);

        if(update.getRelations() != null) {
            for (String rel : update.getRelations().keySet()) {
                if (!relations.contains(rel.toUpperCase())) {
                    response.setAnswer(error("The relationship type '" + rel + "' doesn't exist in the DB"));
                    return;
                }
            }
        }

        Neo4j.updateNode(update.getId(), update.getRelations(), update.getProperties());

        response.setAnswer(ok());
    };

    /**
     * Get Service Handler.
     * Parses the header and payload into a get object with Gson.
     * Calls method to query Neo4j.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> getServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class); // {"user":"userName","datetime":"timestamp"}
        Get get = parser.fromJson(request.getPayload(), Get.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info(gson.toJson(get));
        // {"label":"someLabel","id":someID, "relations":{"type": "friend_of", "id": 000000},
        // "properties":{"name":"someName","surname":"someSurname"}}
        if (get.getId() != 0) {
            // {'id':someID}
            response.setAnswer(Neo4j.getNodeById(get.getId()));
        } else {
            response.setAnswer(Neo4j.getNode(get.getLabel(), get.getRelations(), get.getProperties()));
        }
    };

    /**
     * Cypher Service Handler.
     * Proceeds with a plain Cypher query.
     * Calls method to query Neo4j
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> cypherServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        logger.info(request.getPayload());
        response.setAnswer(Neo4j.run(request.getPayload()));
    };

    /**
     * Remove Service Handler.
     * Parses the header and payload into a remove object.
     * Calls method to query Neo4j.
     */
    static ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> removeServiceHandler = (request, response) -> {
        Header header = parser.fromJson(request.getHeader(), Header.class);
        Remove remove = parser.fromJson(request.getPayload(), Remove.class);

        Neo4j.remove(remove.getId(), remove.getRelations(), remove.getProperties());

        response.setAnswer(ok());
    };

}
