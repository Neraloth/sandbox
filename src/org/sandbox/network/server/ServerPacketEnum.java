package org.sandbox.network.server;

import org.apache.mina.core.session.IoSession;
import org.sandbox.Main;

public enum ServerPacketEnum {

    HELLO_CONNECTION("HC"),
    REQUIRE_VERSION("AlEv", Main.clientVersion),
    LOGIN_ERROR("AlEf"),
    ACCESS_DENIED("AlE"),
    LOGIN_ALREADY_CONNECTED("AlEc"),
    LOGIN_DISCONNECT("AlEd"),
    PSEUDO("Ad"),
    COMMUNITY("Ac"),
    HOSTS_LIST("AH", Main.serverId + ";1;110;1"),//([id];[statu];[charge];[connexion possible?])
    LOGIN_OK("AlK"),
    QUESTION("AQ"),
    SELECT_SERVER("AYK"),
    SELECT_SERVER_CRYPT("AXK"),
    SERVER_LIST("AxK"),
    SERVER_QUEUE("Af"),//TODO QUEUE
    BANNED("AlEb");
    
    private String packet;
    private String param;

    ServerPacketEnum(String packet) {
        this.packet = packet;
        this.param = "";
    }

    ServerPacketEnum(String packet, String param) {
        this.packet = packet;
        this.param = param;
    }

    public void send(IoSession session, String param) {
        if(!Main.running)
            return;
        
        session.write(packet + param);
    }

    public void send(IoSession session) {
        if (!Main.running)
            return;
        
        session.write(packet + param);
    }
}
