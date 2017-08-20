package org.roboy.memory.util;
import org.neo4j.driver.v1.*;

import java.util.Iterator;
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
                //TODO: refactor this?
                query = query.substring(0, query.length() - 1);
                query += "})";
                tx.run(query, parameters());
                return true;
            });
        }
    }

    public static String updateRelationships(int id) {
        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return update( tx, id );
                }
            } );
        }
    }

    public static String updateProperties(int id) {
        try (Session session = getInstance().session()) {
            return session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    return update( tx, id );
                }
            } );
        }
    }

    private static String update( Transaction tx, int id )
    {
        StatementResult result = tx.run( "", parameters() );
        return result.toString();
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
        return result.next().get(0).asNode().asMap().toString();
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
        //MATCH (a)-[r0]-(b0)-[r1]->(b1)
        //    WHERE ID(b1) = 158 AND type(r1)=~'STUDY_AT' AND ID(b0) = 5 AND type(r0)=~ 'FRIEND_OF' AND a.name = 'Roboy' AND labels(a) = 'Robot'
        //RETURN a
        String match = "";
        String where = "";

        if (!relations.isEmpty()) {
            if (where == "") {
                where = "WHERE ";
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
        }

        if (!properties.isEmpty()) {
            if (where == "") {
                where = "WHERE ";
            }
            for (Map.Entry<String, String> next : properties.entrySet()) {
                //iterate over the pairs
                where += "a." + next.getKey() + " = " + next.getValue() + " AND ";
            }
        }

        StatementResult result = tx.run( "MATCH (a)" + match + where + " labels(a) = '" + label + "' RETURN ID(a)", parameters() );
        String response = "";
        while (result.hasNext()) {
            response += result.next().get(0).toString() + ", ";
        }
        return response;
    }
}
