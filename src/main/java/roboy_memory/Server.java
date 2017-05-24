package roboy_memory;


import org.ros.node.NodeConfiguration;
import roboy_memory.services.vision.AddTwoInts;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This server is responsible for starting ros services.
 */
public class Server {

    private NodeConfiguration nodeConfiguration;

    public Server() throws URISyntaxException {
        URI masterUri = new URI(Config.DEFAULT_MASTER_ADDRESS);

        nodeConfiguration = NodeConfiguration.newPublic(masterUri.getHost(), masterUri);

    }

    public void startServices() {
        AddTwoInts.publish(nodeConfiguration);
    }

}
