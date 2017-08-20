package org.roboy.memory.util;

import org.neo4j.driver.v1.*;

import java.util.Map;

import static org.roboy.memory.util.Config.*;

public class Neo4j implements AutoCloseable {

    private static Neo4j _instance;
    private static Driver _driver;

    private Neo4j() {
        _driver = GraphDatabase.driver(NEO4J_ADDRESS, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
    }

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

    public static void createNode(String label, Map<String, String> parameters) {
        try (Session session = getInstance().session()) {
            session.writeTransaction(tx -> {
                //no prepared statements for now
                String query = "CREATE (a:" + label + "{";
                for (String key : parameters.keySet()) {
                    query += key + ":'" + parameters.get(key) + "',";
                }
                //TODO: refactor this shit
                query = query.substring(0, query.length() - 1);
                query += "})";
                tx.run(query, parameters());
                return true;
            });
        }
    }

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

    private static String matchNodeById( Transaction tx, int id )
    {
        StatementResult result = tx.run( "MATCH (a) where ID(a)=$id RETURN a", parameters( "id", id ) );
        return result.toString();
    }

    public static String getNode(String label, Map<String, String> relations, Map<String, String> properties) {
        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return matchNode( tx, label, relations, properties );
                }
            } );
        }
    }

    private static String matchNode( Transaction tx, String label, Map<String, String> relations, Map<String, String> properties )
    {
        String queryParams = "";
        StatementResult result = tx.run( "MATCH (a:$label) where " + queryParams + " RETURN ID(a)", parameters( "label", label ) );
        return result.toString();
    }
}
