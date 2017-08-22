package org.roboy.memory.util;
import org.neo4j.driver.v1.*;
import redis.clients.jedis.Jedis;

import javax.print.attribute.IntegerSyntax;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;

import static org.roboy.memory.util.Config.*;

/**
 * Contains the cypher queries for GET, CREATE and UPDATE functions
 *
 */
public class Neo4j implements AutoCloseable {

    private static Neo4j _instance;
    private static Driver _driver;
    private static Jedis jedis;

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

    public static void run(String query, Value parameters) {
        try (Session session = getInstance().session()) {
            session.run(query, parameters);
        }
    }

    //for cypher
    public static void run(String query) {
        try (Session session = getInstance().session()) {
            session.run(query);
        }
    }


    /**
     * Create
     *
     * @param label
     * @param faceVector
     * @param parameters
     * @return
     */
    public static String createNode(String label, String faceVector, Map<String, String> parameters) {
        try (Session session = getInstance().session()) {
            jedis = new Jedis(URI.create(REDIS_URI));
            StatementResult result = session.writeTransaction(tx -> {
                //no prepared statements for now
                String query = "CREATE (a:" + label;
                    query += "{";
                    for (String key : parameters.keySet()) {
                        query += key + ":'" + parameters.get(key) + "',";
                    }
                    query = query.substring(0, query.length() - 1);
                    query += "}";
                //TODO: refactor this?
                query += ") RETURN ID(a)";
                return  tx.run(query, parameters());
            });
            String id = result.next().get(0).toString();

            if (faceVector != null) {
                jedis.auth(REDIS_PASSWORD);
                jedis.set(id, faceVector);
            }

            return id;
        }
    }


    /**
     * Update
     *
     * @param id
     * @param relations
     * @param properties
     * @return
     */
    public static String updateNode(int id, Map<String, String> relations, Map<String, String> properties) {
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

    private static String update( Transaction tx, int id, Map<String, String> relations, Map<String, String> properties) {

        String query = "MATCH (a)";
        String where = "WHERE ID(a)=" + id;

        if(relations != null) { //add relations
            String create = "";
            int i = 1;
            for (String key : relations.keySet()) {
                query += ",(b" + i + ")";
                where +=  " AND ID(b" + i + ") = " + relations.get(key);
                create += " CREATE (a)-[r" + i + ":" + key +"]->(b" + i + ") ";
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

        System.out.println("Query: " + query); //Match (n) where ID(n)=$id Set n.surname = 'bla' Set n.birthdate = '30.06.1994' Set n.sex = 'male' Return n

        StatementResult result = tx.run( query, parameters() );
        //StatementResult result = tx.run( "Match ...", parameters("id", id) );
        return result.toString();
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
        System.out.println(query);
        StatementResult result = tx.run(query, parameters() );
        return result.next().get(0).asNode().asMap().toString();
    }

    public static String getNode(String label, Map<String, String> relations, Map<String, String> properties) {

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

    private static String matchNode( Transaction tx, String label, Map<String, String> relations, Map<String, String> properties ) {

        //MATCH (a)-[r1]-(b1)-[r2]->(b2)
        //    WHERE ID(b1) = 158 AND type(r1)=~'STUDY_AT' AND ID(b0) = 5 AND type(r0)=~ 'FRIEND_OF' AND a.name = 'Roboy' AND labels(a) = 'Robot'
        //RETURN a
        String match = "";
        String where = "";

        if (relations != null) {
            if (where == "") {
                where = " WHERE ";
            }

            int i = 1;
            for (Map.Entry<String, String> next : relations.entrySet()) {
                //iterate over the pairs
                if (i < relations.size()) {
                    match += "-[r" + i + "]-(b" + i + ")";
                } else {
                    match += "-[r" + i + "]->(b" + i + ")";
                }
                where += "type(r" + i + ")=~ '" + next.getKey() + "' AND ID(b" + i + ") = " + next.getValue() + " AND ";

                i++;
            }
            where = where.substring(0, where.length() - 4);
        }

        if (properties != null) {
            if (where == "") {
                where = " WHERE ";
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
        System.out.println(query);
        StatementResult result = tx.run(query, parameters() );
        String response = "";
        while (result.hasNext()) {
            response += result.next().get(0).toString() + ", ";
        }
        return response;
    }
}
