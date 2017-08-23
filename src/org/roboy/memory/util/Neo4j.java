package org.roboy.memory.util;
import com.google.gson.Gson;
import org.neo4j.driver.internal.types.InternalTypeSystem;
import org.neo4j.driver.v1.*;
import redis.clients.jedis.Jedis;


import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import static org.roboy.memory.util.Config.*;

/**
 * Contains the cypher queries for GET, CREATE and UPDATE functions
 *
 */
public class Neo4j implements AutoCloseable {

    private static Neo4j _instance;
    private static Driver _driver;
    private static Jedis jedis;
    private static Gson parser = new Gson();
    private static Logger logger = Logger.getLogger(Neo4j.class.toString());

    private Neo4j() {
        _driver = GraphDatabase.driver(NEO4J_ADDRESS, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
    }

    /**
     *
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
            StatementResult result = session.run(query);
            String response = "";
            Value value;
            logger.info(query);
            while (result.hasNext()) {
                value = result.next().get(0);
                if (value.hasType(InternalTypeSystem.TYPE_SYSTEM.NODE())) { //if response is Node
                    response += value.asMap().toString() + ", ";
                } else { //if responce is String
                    response += value.toString() + ", ";
                }
            }
            logger.info(response);
            return response;
        }
    }


    /**
     * Create
     *
     * @param label
     * @param faceVector
     * @param properties
     * @return
     */
    public static String createNode(String label, String[] faceVector, Map<String, String> properties) {
        try (Session session = getInstance().session()) {
            return createNode(session, properties, faceVector, label);
        }
    }

    private static String createNode(Session session, Map<String, String> properties, String[] faceVector, String label) {
        StatementResult result = session.writeTransaction(tx -> {
            //no prepared statements for now
            String query = "CREATE (a:" + label;
            query += "{";
            for (String key : properties.keySet()) {
                query += key + ":'" + properties.get(key) + "',";
            }
            query = query.substring(0, query.length() - 1);
            query += "}";
            //TODO: refactor this?
            query += ") RETURN ID(a)";
            logger.info(query);
            return  tx.run(query, parameters());
        });
        String id = result.next().get(0).toString();

        if (faceVector != null) {
            jedis = new Jedis(URI.create(REDIS_URI));
            jedis.auth(REDIS_PASSWORD);
            for (String vector : faceVector) {
                jedis.sadd(id, vector);
            }
        }

        logger.info(id);
        return id;
    }


    /**
     * Update
     *
     * @param id
     * @param relations
     * @param properties
     * @return
     */
    public static String updateNode(int id, Map<String, String[]> relations, Map<String, String> properties) {
        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return update( tx, id, relations, properties);
                }
            } );
        }
    }

    private static String update( Transaction tx, int id, Map<String, String[]> relations, Map<String, String> properties) {

        String query = "MATCH (a)";
        String where = " WHERE ID(a)=" + id;

        if(relations != null) { //add relations
            String create = "";
            int i = 0;
            for (String key : relations.keySet()) {
                for (int j = 0; j < relations.get(key).length; j++) {
                    String relID = relations.get(key)[j];
                    query += ",(b" + i + j + ")";
                    where += " AND ID(b" + i + j + ") = " + relID;
                    create += " CREATE (a)-[r" + i + j + ":" + key +"]->(b" + i + j + ") ";
                }
                i++;
            }
            where += create;
        }

        if (properties != null) { //set properties
            String set = "";
            for (String key : properties.keySet()) {
                if (properties.get(key).matches("^[0-9]*$")) { //if property is int
                    set += " Set a." + key + " = " + properties.get(key); //without ''
                } else {
                    set += " Set a." + key + " = '" + properties.get(key) + "'"; //just Strings, no int
                }
            }
            where += set;
        }
        query += where + " Return a";

        logger.info(query);

        StatementResult result = tx.run( query, parameters() );
        String response = "";
        if (result.hasNext()) {
            response = parser.toJson(result.next().get(0).asMap());
        }
        logger.info(response);
        return response;
    }


    /**
     * Get
     *
     * @param id
     * @return
     */
    public static String getNodeById(int id) {

        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return matchNodeById( tx, id );
                }
            } );
        }
    }

    private static String matchNodeById( Transaction tx, int id ) {

        String query = "MATCH (a) where ID(a)=" + id + " RETURN a";
        String queryRelations = "MATCH (a)-[r]-(b) WHERE ID(a) = " + id +" Return type(r),ID(b)";
        HashMap<String, HashSet<String>> relAndIDs = new HashMap<String, HashSet<String>>();

        logger.info(query);
        logger.info(queryRelations);

        StatementResult result = tx.run(query, parameters() ); //run query
        String node = "'properties': " + parser.toJson(result.next().get(0).asMap());

        StatementResult resultRelations = tx.run(queryRelations, parameters() ); //run queryRelations
        String relationResponse = ", 'relations': ";
        while (resultRelations.hasNext()) {
            Record next = resultRelations.next();
            String key = next.get(0).toString();
            String value = next.get(1).toString();
            if (!relAndIDs.containsKey(next.get(0).toString())) {
                relAndIDs.put(key,new HashSet<String>(Arrays.asList(next.get(1).toString())));
            } else {
                relAndIDs.get(key).add(value);
            }
        }
        relationResponse += relAndIDs.toString().replace('=',':');


        logger.info(node + relationResponse);
        return node + relationResponse;
    }

    public static String getNode(String label, Map<String, String[]> relations, Map<String, String> properties) {

        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    return matchNode( tx, label, relations, properties );
                }
            } );
        }
    }

    private static String matchNode( Transaction tx, String label, Map<String, String[]> relations, Map<String, String> properties ) {

        //MATCH (a)-[r1]-(b1)-[r2]->(b2)
        //    WHERE ID(b1) = 158 AND type(r1)=~'STUDY_AT' AND ID(b0) = 5 AND type(r0)=~ 'FRIEND_OF' AND a.name = 'Roboy' AND labels(a) = 'Robot'
        //RETURN a
        String match = "";
        String where = "";


        if (relations != null) {
            if (Objects.equals(where, "")) {
                where = " WHERE ";
            }

            int i = 0;
            for (Map.Entry<String, String[]> next : relations.entrySet()) {
                //iterate over the pairs
                for (int j = 0; j < next.getValue().length; j++) {
                    String relID = next.getValue()[j];
                    if (i < relations.size() - 1) {
                        match += "-[r" + i + j + "]-(b" + i + j + ")";
                    } else {
                        match += "-[r" + i + j + "]->(b" + i + j + ")";
                    }
                    where += "type(r" + i + j + ")=~ '" + next.getKey();
                    where += "' AND ID(b" + i + j + ") = " + relID;
                }

                where += " AND ";

                i++;
            }
            where = where.substring(0, where.length() - 4);
        }

        if (properties != null) {
            if (Objects.equals(where, "")) {
                where = " WHERE ";
            } else {
                where += "AND ";
            }

            for (Map.Entry<String, String> next : properties.entrySet()) {
                //iterate over the pairs
                if (next.getValue().matches("^[0-9]*$")) {
                    where += "a." + next.getKey() + " = " + next.getValue() + " AND ";
                } else {
                    where += "a." + next.getKey() + " = '" + next.getValue() + "' AND ";
                }
            }
            where = where.substring(0, where.length() - 5);
        }

        String query = "MATCH (a:" + label + ")" + match + where + " RETURN ID(a)";
        logger.info(query);
        StatementResult result = tx.run(query, parameters() );
        String response = "";
        while (result.hasNext()) {
            response += result.next().get(0).toString() + ", ";
        }
        response = response.substring(0, response.length() - 2);
        response = "\"[" + response + "]\"";
        logger.info(response);
        return response;
    }

    /**
     * Remove
     *
     * @param id
     * @param relations
     * @param properties
     * @return
     */
    public static String remove(int id, Map<String, String[]> relations, String[] properties) {
        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return removeRelsProps( tx, id, relations, properties);
                }
            } );
        }
    }

    private static String removeRelsProps( Transaction tx, int id, Map<String, String[]> relations, String[] properties) {

        String query = "";
        String where = " WHERE ID(a)=" + id;
        String delete = "";
        String remove = "";


        //Match (n)-[r1:LIVE_IN]->(b1),(n)-[r2:LIVE_IN]->(b2) where ID(n)=131 AND ID(b1)=78 AND ID(b2)=57 Delete r1,r2 Remove n.abc,n.xyz
        if(relations != null) { //delete relations
            query = "MATCH ";
            delete = " Delete ";
            int i = 0;
            for (String key : relations.keySet()) {
                for (int j = 0; j < relations.get(key).length; j++) {
                    String relID = relations.get(key)[j];
                    delete += "r" + i + j + ",";
                    where +=  " AND ID(b" + i + j + ") = " + relID;
                    query += "(a)-[r" + i + j + ":" + key +"]->(b" + i + j + "), ";
                }
                i++;
            }
            query = query.substring(0,query.length()-2);
            delete = delete.substring(0,delete.length()-1);
        }

        //Match n where ID(n)=1 REMOVE n.key
        if (properties != null) { //delete properties
            if (Objects.equals(query, "")) {
                query = "MATCH (a) ";
            }
            remove = " Remove ";
            for (String key : properties) {
                System.out.println(key);
                if (!Objects.equals(key, "name")) {
                    System.out.println(key);
                    remove += "a." + key + ", ";
                }
            }
            remove = remove.substring(0,remove.length()-2);
        }

        query += where + delete + remove;

        logger.info(query);

        StatementResult result = tx.run( query, parameters() );
        logger.info(result.toString());
        return result.toString();
    }

}
