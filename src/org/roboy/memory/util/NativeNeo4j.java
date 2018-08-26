package org.roboy.memory.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.neo4j.driver.v1.*;
import org.roboy.memory.interfaces.Memory;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;
import org.roboy.ontology.NodeModel;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.roboy.memory.util.Config.*;

/**
 * Contains the methods for running MemoryNodeModel queries.
 * Talks to the Neo4j and Redis databases.
 * Handles the result retrieved from Neo4j.
 */
public class NativeNeo4j implements AutoCloseable {

    private static NativeNeo4j _instance;
    private static Driver _driver;
    private static Jedis jedis;
    private static Gson gson = new Gson();
    private static Logger logger = Logger.getLogger(NativeNeo4j.class.toString());
    private static boolean initialized = false;

    private NativeNeo4j() {
        this.initDriver();
    }

    /**
     * Singleton for the Neo4j class
     *
     * @return Neo4J Driver instance if the object of Neo4j class is initialized
     */
    public static Driver getInstance() {
        if (_instance == null) {
            _instance = new NativeNeo4j();
        }

        return _instance.getDriver();
    }

    /**
     * Getter for the Neo4j driver instance
     *
     * @return Neo4J Driver instance
     */
    private Driver getDriver() {
        if (_driver == null) {
            this.initDriver();
        }

        return _driver;
    }

    /**
     * Init driver
     */
    private void initDriver() {
        try {
            _driver = GraphDatabase.driver(NEO4J_ADDRESS, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
            initialized = true;
        } catch (Exception e) {
            logger.warning("Could not init Neo4j driver. " + e.getMessage());
            System.out.print("MEMORY INIT: Could not initialize Neo4j driver. Check if Neo4j is running!");
            initialized = false;
        }
    }

    /**
     * Check if the driver is initialized
     */
    public boolean isInitialized(){
        return initialized;
    }

    @Override
    public void close() throws Exception {
        _driver.close();
        _instance = null;
    }

    /**
     * Wrapper for the Neo4j query parameters
     *
     * @return Set of keys and values for parameters
     */
    public static Value parameters(Object... keysAndValues) {
        return Values.parameters(keysAndValues);
    }


    /**
     * Method accepting JSON Create queries
     *
     * @return result obtained by createNode method
     */
    public static Integer createNode(MemoryNodeModel create) {
        try (Session session = getInstance().session()) {
            return createNode(session, create);
        }
    }

    /**
     * Method processing JSON Create queries
     *
     * @param session is a session handler for transaction handling to query Neo4j DB
     * @return ID of the node that was created in Neo4j DB
     */
    private static Integer createNode(Session session, MemoryNodeModel create) {
        StatementResult result = session.writeTransaction(tx -> {
            //TODO: prepared statement
            NativeQueryBuilder builder = new NativeQueryBuilder();

            builder.add("CREATE (a:" + create.getLabel() + " ");
            builder.addParameters(create.getProperties());
            builder.add(") RETURN ID(a)");

            String query = builder.getQuery();
            logger.info(query);
            return tx.run(query, parameters());
        });

//        if (create.getFace() != null) {
//            saveToJedis(create.getFace(), id);
//        }

        Integer id = result.single().get(0).asInt();
        logger.info("Retrieved ID: " + id.toString());

        return id;
    }

    private static void saveToJedis(String[] face, String id) {
        jedis = new Jedis(URI.create(REDIS_URI));
        jedis.auth(REDIS_PASSWORD);
        for (String vector : face) {
            jedis.sadd(id, vector);
        }
        jedis.disconnect();
    }


    /**
     * Method accepting JSON Update queries
     *
     * @return result obtained by update method
     */
    public static boolean updateNode(MemoryNodeModel update) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> update(tx, update));
        }
    }

    /**
     * Method processing JSON Update queries
     *
     * @param tx is a transaction handler to query Neo4j DB
     * @return response from Neo4j upon updating the node
     */
    private static boolean update(Transaction tx, MemoryNodeModel update) {
        //update properties
        boolean result = false;
        if (update.getProperties() != null) {
            NativeQueryBuilder paramUpdate = new NativeQueryBuilder();
            paramUpdate.matchById(update.getId(), "n");
            paramUpdate.set(update.getProperties(), "n");
            paramUpdate.add("RETURN n");
            logger.info(paramUpdate.getQuery());
            StatementResult statementResult = tx.run(paramUpdate.getQuery(), parameters());
            logger.info("Properties updated:" + statementResult.summary().counters().containsUpdates());
            result = true;
        }

        //Create new relationships
        if (update.getRelationships() != null) {
            NativeQueryBuilder builder = new NativeQueryBuilder();
            builder.matchById(update.getId(), "n");
            int counter = 0;
            for (Map.Entry<Neo4jRelationship, ArrayList<Integer>> entry : update.getRelationships().entrySet()) {
                builder.add("MATCH (m%d) WHERE ID(m%d) IN %s ", counter, counter, gson.toJson(entry.getValue()));
                counter++;
            }
            counter = 0;
            for (Map.Entry<Neo4jRelationship, ArrayList<Integer>> entry : update.getRelationships().entrySet()) {
                //counter--;
                builder.add("MERGE (n)-[r%d:%s]-(m%d)", counter, entry.getKey().type, counter);
                counter++;
            }
            builder.add("RETURN n");
            logger.info(builder.getQuery());
            StatementResult statementResult = tx.run(builder.getQuery(), parameters());
            logger.info("Relationships created:" + statementResult.summary().counters().relationshipsCreated());
            result = true;
        }

        return result;
    }


    /**
     * Method accepting JSON Get by ID queries
     *
     * @param id is a unique pointer to the node in Neo4j DB
     * @return result obtained by matchNodeById method
     */
    public static MemoryNodeModel getNodeById(int id, Neo4jMemoryInterface memory) {

        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> matchNodeById(tx, id, memory));
        }
    }

    /**
     * Method processing JSON Get by ID queries
     *
     * @param tx is a transaction handler to query Neo4j DB
     * @param id is a unique pointer to the node in Neo4j DB
     * @return a JSON object containing node labels, properties and relationships
     */
    private static MemoryNodeModel matchNodeById(Transaction tx, int id, Neo4jMemoryInterface memory) {

        NativeQueryBuilder builder = new NativeQueryBuilder();
        builder.matchById(id, "n").add("RETURN PROPERTIES(n)");
        String query = builder.getQuery();
        logger.info(query);
        StatementResult result = tx.run(builder.getQuery(), parameters());
        List<Record> records = result.list();

        if(records.size() == 0) {
            return null;
        }

        HashMap<Neo4jProperty, Object> properties = new HashMap<>();
        for(Map.Entry<String, Object> entry : records.get(0).get(0).asMap(Value::asObject).entrySet()) {
            Neo4jProperty key = Neo4jProperty.lookupByType(entry.getKey());
            if (key != null) {
                properties.put(key, entry.getValue());
            }
        }

        //get relationships
        builder = new NativeQueryBuilder();
        query = builder.add("MATCH (n)-[r]-(m) WHERE ID(n)=%d RETURN TYPE(r) as name, COLLECT(ID(m)) as ids", id).getQuery();
        logger.info(query);
        result = tx.run(builder.getQuery(), parameters());
        records = result.list();

        HashMap<Neo4jRelationship, ArrayList<Integer>> relationships = new HashMap<>();
        if(records.size() > 0) {
            for (Record record : records) {
                Neo4jRelationship key = Neo4jRelationship.lookupByType(record.get("name").asString());
                if (key != null) {
                    relationships.put(key, new ArrayList<>(record.get("ids").asList(Value::asInt)));
                }
            }
        }

        MemoryNodeModel node = createNode(memory, id, properties.isEmpty() ? null : properties, relationships.isEmpty() ? null : relationships);
        logger.info("Node retrieved: " + node.toString());
        return node;
    }

    private static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    private static MemoryNodeModel createNode(Neo4jMemoryInterface memory, int id, HashMap<Neo4jProperty, Object> properties,
                                              HashMap<Neo4jRelationship, ArrayList<Integer>> relationships) {
        MemoryNodeModel node = new MemoryNodeModel(memory);
        node.setId(id);
        node.setProperties(properties);
        node.setRelationships(relationships);
        return node;
    }

    public static ArrayList<MemoryNodeModel> getNode(MemoryNodeModel get, Neo4jMemoryInterface memory) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> matchNode(tx, get, memory));
        }
    }

    private static ArrayList<MemoryNodeModel> matchNode(Transaction tx, MemoryNodeModel get, Neo4jMemoryInterface memory) {
        NativeQueryBuilder builder = new NativeQueryBuilder();
        if(get.getLabel() != null) logger.info("Class label: " + get.getLabel().toString());
        if(get.getProperties() != null) logger.info("Properties: " + get.getProperties().toString());
        if(get.getRelationships() != null) logger.info("Relationships: " + get.getRelationships().toString());
        builder.add("MATCH (n:%s", get.getLabel());
        builder.addParameters(get.getProperties());
        builder.add(")");
        if(get.getRelationships() != null) {
            int counter = 0;
            for (Map.Entry<Neo4jRelationship, ArrayList<Integer>> entry : get.getRelationships().entrySet()) {
                builder.add("MATCH (n)-[r%d:%s]-(m%d) WHERE ID(m%d) IN %s", counter, entry.getKey().type, counter, counter, gson.toJson(entry.getValue()));
                counter++;
            }
        }
        builder.add("RETURN COLLECT(ID(n)) AS ids");
        logger.info(builder.getQuery());
        StatementResult result = tx.run(builder.getQuery(), parameters());
        List<Record> records = result.list();

        List<Integer> ids = records.get(0).get(0).asList(Value::asInt);
        logger.info("Retrieved array of ids: " + ids.toString());
        ArrayList<MemoryNodeModel> nodes = new ArrayList<>();
        for (Integer id : ids) {
            nodes.add(getNodeById(id, memory));
        }

        return nodes;
    }

    /**
     * Method accepting JSON Remove queries
     *
     * @return result obtained by removeRelsProps method
     */
    public static boolean remove(MemoryNodeModel remove) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> remove(tx, remove));
        }
    }

    /**
     * Method processing JSON Remove queries
     *
     * @param tx is a transaction handler to query Neo4j DB
     * @return response from Neo4j upon removing the specified relationships and properties
     */
    private static boolean remove(Transaction tx, MemoryNodeModel remove) {
        //remove properties
        boolean response = false;
        if (remove.getProperties() != null) {
            NativeQueryBuilder builder = new NativeQueryBuilder();
            builder.matchById(remove.getId(), "n");
            for (Neo4jProperty property : remove.getProperties().keySet()) {
                builder.add("REMOVE n.%s", property.type);
            }
            builder.add("RETURN n");
            String query = builder.getQuery();
            logger.info(query);
            StatementResult result = tx.run(query, parameters());
            logger.info("Properties removed: " + result.summary().counters().containsUpdates());
            response = true;
        }

        if(remove.getRelationships() != null) {
            NativeQueryBuilder builder = new NativeQueryBuilder();
            builder.matchById(remove.getId(), "n");
            int counter = 0;
            for (Map.Entry<Neo4jRelationship, ArrayList<Integer>> entry : remove.getRelationships().entrySet()) {
                builder.add("MATCH (n)-[r%d:%s]-(m%d) WHERE ID(m%d) IN %s", counter, entry.getKey(), counter, counter, gson.toJson(entry.getValue()));
                counter++;
            }
            builder.add("DELETE r%d", --counter);
            while(counter > 0) {
                builder.add(",r%d", --counter);
            }
            String query = builder.getQuery();
            logger.info(query);
            StatementResult result = tx.run(query, parameters());
            logger.info("Relationships removed: " + result.summary().counters().relationshipsDeleted());
            response = true;
        }

        logger.info("Removed entries: " + response);
        return response;
    }
}
