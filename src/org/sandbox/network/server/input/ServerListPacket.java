package org.sandbox.network.server.input;

import org.apache.mina.core.session.IoSession;
import org.sandbox.Main;
import org.sandbox.character.Account;
import org.sandbox.models.DAOFactory;
import org.sandbox.network.GeneratorsRegistry;
import org.sandbox.network.InputPacket;
import org.sandbox.network.SessionAttributes;
import org.sandbox.network.server.ServerPacketEnum;

public class ServerListPacket implements InputPacket {

    @Override
    public String id() {
        return "Ax";
    }

    @Override
    public void perform(String extra, IoSession session) {
        Account account = SessionAttributes.ACCOUNT.getValue(session);
        
        if(account == null)
            return;
        
        ServerPacketEnum.SERVER_LIST.send(
                session,
                GeneratorsRegistry.getAccount().generateServerList(
                		31536000000L, 
                        Main.serverId, 
                        DAOFactory.character().countByAccount(account.id)
                )
        );
    }
    
}
