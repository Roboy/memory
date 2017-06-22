package memory.util;

import org.neo4j.driver.v1.*;

import java.util.HashMap;

import static memory.util.Config.*;

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

    /*
        Wrappers for data types
     */

    public static void run(String query, Value parameters) {
        try (Session session = getInstance().session()) {
            session.run(query, parameters);
        }
    }

    public static String getStringWrite(String query, Value parameters) {
        try (Session session = getInstance().session()) {
            return session.writeTransaction(tx -> {
                StatementResult result = tx.run(query, parameters);
                return result.single().values().get(0).asString();
            });
        }
    }
}
