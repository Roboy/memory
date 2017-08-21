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

    //for cypher
    public static void run(String query) {
        try (Session session = getInstance().session()) {
            session.run(query);
        }
    }

    //Create
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

    //Update
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

    private static String update( Transaction tx, int id, Map<String, String> relations, Map<String, String> properties)
    {
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

    //Get
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
        String queryParams = "";
        StatementResult result = tx.run( "MATCH (a:$label) where " + queryParams + " RETURN ID(a)", parameters( "label", label ) );
        return result.toString();
    }
}
