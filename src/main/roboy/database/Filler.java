package database;

import memory.util.Neo4j;

public class Filler {

    public static void main(String[] args) {

        String s = Neo4j.getStringWrite("MATCH (n) RETURN n LIMIT 1", null);
    }

}
