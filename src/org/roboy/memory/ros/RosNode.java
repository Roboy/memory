package org.roboy.memory.ros;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import roboy_communication_cognition.DataQuery;


/**
 * ROS Service for saving data object to DB. Data is received as JSON object.
 * JSON object is parsed using Parser (not implemented) and saved to neo4j.
 */
public class RosNode extends AbstractNodeMain {
    private static String name = "/roboy/cognition/memory";

    static void register(NodeConfiguration nodeConfiguration, NodeMainExecutor nodeMainExecutor) {
        nodeConfiguration.setNodeName(name);
        nodeMainExecutor.execute(new RosNode(), nodeConfiguration);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        connectedNode.newServiceServer(this.getDefaultNodeName() + "/create", DataQuery._TYPE, ServiceLogic.createServiceHandler);
        connectedNode.newServiceServer(this.getDefaultNodeName() + "/update", DataQuery._TYPE, ServiceLogic.updateServiceHandler);
        connectedNode.newServiceServer(this.getDefaultNodeName() + "/get", DataQuery._TYPE, ServiceLogic.getServiceHandler);
        connectedNode.newServiceServer(this.getDefaultNodeName() + "/cypher", DataQuery._TYPE, ServiceLogic.cypherServiceHandler);
    }

}
