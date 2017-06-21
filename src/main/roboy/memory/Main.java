package memory;


import memory.util.Neo4j;

import java.net.URISyntaxException;

public class Main
{
    public static void main( String[] args ) throws URISyntaxException {
        RosRun server = new RosRun();
        server.start();
    }
}
