package org.sandbox.network.server.input;

import org.apache.mina.core.session.IoSession;
import org.sandbox.Main;
import org.sandbox.character.Account;
import org.sandbox.network.InputPacket;
import org.sandbox.network.SessionAttributes;
import org.sandbox.network.server.ServerPacketEnum;

public class SelectServerPacket implements InputPacket {

    @Override
    public String id() {
        return "AX";
    }

    @Override
    public void perform(String extra, IoSession session) {
        Account account = SessionAttributes.ACCOUNT.getValue(session);

        if (account == null) {
            session.close(true);
            return;
        }

        String ticket = account.setWaiting();

        String p = Main.ip + ";" + Main.gamePort + ";" + ticket;
        ServerPacketEnum.SELECT_SERVER.send(session, p);
        
    }
    
}
