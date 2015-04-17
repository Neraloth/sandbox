package org.sandbox.network.server;

import org.sandbox.network.InputPacketsHandler;
import org.sandbox.network.server.input.SelectServerPacket;
import org.sandbox.network.server.input.ServerListPacket;

public class ServerInputHandler extends InputPacketsHandler {
	
    static private ServerInputHandler instance = new ServerInputHandler();
    
    public ServerInputHandler() {
        registerPacket(new ServerListPacket());
        registerPacket(new SelectServerPacket());
    }
    
    static public ServerInputHandler instance(){
        return instance;
    }
}
