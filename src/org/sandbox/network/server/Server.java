package org.sandbox.network.server;

import java.io.IOException;

import org.sandbox.Console;
import org.sandbox.Main;
import org.sandbox.network.MinaServer;

public class Server {

    protected MinaServer server;
    protected static Server instance = null;

    private Server() {
        try {
            server = new MinaServer(Main.serverPort, new ServerIoHandler());
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    public static void start() {
        if (instance != null) {
        	Console.info("the server is already running");
        }
        instance = new Server();
    }

    public static void stop() {
        instance.server.stop();
    }
}
