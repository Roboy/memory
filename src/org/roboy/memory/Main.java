package org.roboy.memory;


import org.roboy.memory.ros.RosRun;

import java.net.URISyntaxException;

public class Main
{
    public static void main( String[] args ) throws URISyntaxException {
        RosRun server = new RosRun();
        server.start();
    }
}
