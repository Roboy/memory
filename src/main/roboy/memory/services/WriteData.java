package memory.services;

import memory.Parser;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;


/**
 * ROS Service for saving data object to DB. Data is received as JSON object.
 * JSON object is parsed using Parser (not implemented) and saved to neo4j.
 */
public class WriteData extends AbstractNodeMain {
    private static String name = "/roboy/cognition/memory/data/write";
    private Parser parser = new Parser();

    public static void publish(NodeConfiguration nodeConfiguration, NodeMainExecutor nodeMainExecutor) {
        nodeConfiguration.setNodeName(name);
        nodeMainExecutor.execute(new WriteData(), nodeConfiguration);
    }

    private ServiceResponseBuilder<DataQueryRequest, DataQueryResponse> handler = (request, response) -> {
        //logic here
        parser.parse(request.getPayload());
        response.setAnswer("");
    };

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        connectedNode.newServiceServer(this.getDefaultNodeName(), DataQuery._TYPE, handler);
    }

}
