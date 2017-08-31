package org.roboy.memory.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.neo4j.driver.v1.*;
import org.roboy.memory.models.*;
import redis.clients.jedis.Jedis;


import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import static org.roboy.memory.util.Config.*;

/**
 * Contains the cypher queries for GET, CREATE and UPDATE functions
 */
public class Neo4j implements AutoCloseable {

    private static Neo4j _instance;
    private static Driver _driver;
    private static Jedis jedis;
    private static Gson gson = new Gson();
    private static Logger logger = Logger.getLogger(Neo4j.class.toString());

    private Neo4j() {
        _driver = GraphDatabase.driver(NEO4J_ADDRESS, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
    }

    /**
     * @return returns Neo4J Driver
     */
    public static Driver getInstance() {
        if (_instance == null) {
            _instance = new Neo4j();
        }

        return _instance.getDriver();
    }

    private Driver getDriver() {
        return _driver;
    }

    @Override
    public void close() throws Exception {
        _driver.close();
    }

    //parameters wrapper
    public static Value parameters(Object... keysAndValues) {
        return Values.parameters(keysAndValues);
    }

    //run only cypher service
    public static String run(String query) {
        try (Session session = getInstance().session()) {
            logger.info(query);
            StatementResult result = session.run(query);
            String response = gson.toJson(result.list());
            logger.info(response);
            return response;
        }
    }


    /**
     * Create
     */
    public static String createNode(Create create) {
        try (Session session = getInstance().session()) {
            return createNode(session, create);
        }
    }

    private static String createNode(Session session, Create create) {
        StatementResult result = session.writeTransaction(tx -> {
            //TODO: prepared statement
            QueryBuilder builder = new QueryBuilder();

            builder.add("CREATE (a:" + create.getLabel() + " ");
            builder.addParameters(create.getProperties());
            builder.add(") RETURN ID(a)");

            String query = builder.getQuery();
            logger.info(query);
            return tx.run(query, parameters());
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", result.single().get(0).asInt());
        //todo: check why do we need get?
        String id = jsonObject.get("id").getAsString();

        if (create.getFace() != null) {
            saveToJedis(create.getFace(), id);
        }

        logger.info(id);
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
     * Update
     */
    public static String updateNode(Update update) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> update(tx, update));
        }
    }

    private static String update(Transaction tx, Update update) {
        //todo: wrong place for return statements
        //update properties
        JsonObject result = new JsonObject();
        if (update.getProperties() != null) {
            QueryBuilder paramUpdate = new QueryBuilder();
            paramUpdate.matchById(update.getId(), "n");
            paramUpdate.set(update.getProperties(), "n");
            paramUpdate.add("RETURN n");
            logger.info(paramUpdate.getQuery());
            StatementResult statementResult = tx.run(paramUpdate.getQuery(), parameters());
            int propNum = statementResult.summary().counters().propertiesSet();
            if (propNum < 1) {
                throw new NullPointerException();
            }
            result.addProperty("properties updated", statementResult.summary().counters().containsUpdates());

            logger.info(result.get("properties updated").getAsString());
            return result.get("properties updated").getAsString();
        }



        //create new relations
        if (update.getRelationships() != null) {
            QueryBuilder builder = new QueryBuilder();
            builder.matchById(update.getId(), "n");
            int counter = 0;
            for (Map.Entry<String, int[]> entry : update.getRelationships().entrySet()) {
                builder.add("MATCH (m%d) WHERE ID(m%d) IN %s ", counter, counter, gson.toJson(entry.getValue()));
                builder.add("MERGE (n)-[r%d:%s]-(m%d)", counter, entry.getKey(), counter);
                counter++;
            }
            builder.add("RETURN n");
            logger.info(builder.getQuery());
            StatementResult statementResult = tx.run(builder.getQuery(), parameters());
            result.addProperty("relations created", statementResult.summary().counters().relationshipsCreated());

            logger.info(result.get("relations created").getAsString());
            return result.get("relations created").getAsString();
        }

        logger.info("No relations or properties specified");
        return "No relations or properties specified";
    }


    /**
     * Get
     */
    public static String getNodeById(int id) {

        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> matchNodeById(tx, id));
        }
    }

    private static String matchNodeById(Transaction tx, int id) {
        QueryBuilder builder = new QueryBuilder();
        builder.add("match (n)-[r]-(m) WHERE id(n)=%d return n,type(r) as name,id(m) as id", id);
        String query = builder.getQuery();
        logger.info(query);
        StatementResult result = tx.run(builder.getQuery(), parameters());
        List<Record> records = result.list();

        HashMap<String, String> properties = new HashMap<>();
        for (Map.Entry<String, Object> entry : records.get(0).get(0).asMap().entrySet()) {
            properties.put(entry.getKey(), (String) entry.getValue());
        }

        HashMap<String, ArrayList<Integer>> relationships = new HashMap<>();
        for (Record record : records) {
            String name = record.get("name").asString();
            if (!relationships.containsKey(name)) {
                relationships.put(name, new ArrayList<>());
            }
            relationships.get(name).add(record.get("id").asInt());
        }

        HashMap<String, int[]> relations = new HashMap<>();
//        for (Map.Entry<String, ArrayList<Integer>> entry : relationships.entrySet()) {
//            relations.put(entry.getKey(), (int[]) entry.getValue().toArray());
//        }
        Node node = createNode(id, properties, relations);
        logger.info(gson.toJson(node));
        return gson.toJson(node);
    }

    private  static Node createNode(int id, HashMap<String, String> properties, HashMap<String, int[]> relations) {
        Node node = new Node();
        node.setId(id);
        node.setProperties(properties);
        node.setRelationships(relations);
        return node;
    }

    public static String getNode(Get get) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> matchNode(tx, get));
        }
    }

    private static String matchNode(Transaction tx, Get get) {
        QueryBuilder builder = new QueryBuilder();
        builder.add("MATCH (n:%s", get.getLabel());
        builder.addParameters(get.getProperties());
        builder.add(")");
        int counter = 0;
        for (Map.Entry<String, int[]> entry : get.getRelationships().entrySet()) {
            builder.add("MATCH (n)-[r%d:%s]-(m%d) WHERE ID(m%d) IN %s", counter, counter, counter, gson.toJson(entry.getValue()));
            counter++;
        }
        builder.add("RETURN ID(n)");
        logger.info(builder.getQuery());
        StatementResult result = tx.run(builder.getQuery(), parameters());

        String json = gson.toJson(result.list(record -> record.get(0).asString()).toArray());
        logger.info(json);
        return json;
    }

    /**
     * Remove
     */
    public static String remove(Remove remove) {
        try (Session session = getInstance().session()) {
            return session.readTransaction(tx -> remove(tx, remove));
        }
    }

    private static String remove(Transaction tx, Remove remove) {
        //remove properties
        JsonObject response = new JsonObject();
        if (remove.getPropertiesList().size() > 0) {
            QueryBuilder builder = new QueryBuilder();
            builder.matchById(remove.getId(), "n");
            for (String propery : remove.getPropertiesList()) {
                builder.add("REMOVE n.%s", propery);
            }
            builder.add("RETURN n");
            String query = builder.getQuery();
            logger.info(query);
            StatementResult result = tx.run(query, parameters());
            response.addProperty("properties", result.summary().counters().propertiesSet());
        }

        if(remove.getRelationships().size() > 0) {
            QueryBuilder builder = new QueryBuilder();
            builder.matchById(remove.getId(), "n");
            int counter = 0;
            for (Map.Entry<String, int[]> entry : remove.getRelationships().entrySet()) {
                builder.add("NATCH (n)-[r%d:%s]-(m%d) WHERE ID(m%d) IN %s", counter, entry.getKey(), counter, counter, gson.toJson(entry.getValue()));
                counter++;
            }
            builder.add("REMOVE r%d", --counter);
            while(counter != 0) {
                builder.add(",r%d", --counter);
            }
            String query = builder.getQuery();
            logger.info(query);
            StatementResult result = tx.run(query, parameters());
            response.addProperty("properties", result.summary().counters().propertiesSet());
        }

        logger.info(response.getAsString());
        return response.getAsString();
    }

}
