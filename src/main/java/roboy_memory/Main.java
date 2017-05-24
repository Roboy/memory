package roboy_memory;


import java.net.URISyntaxException;

public class Main
{
    public static void main( String[] args ) throws URISyntaxException {
        Server server = new Server();
        server.startServices();
    }
}
