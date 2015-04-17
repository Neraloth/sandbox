package org.sandbox.network.server;

import org.apache.mina.core.session.IoSession;
import org.sandbox.character.Account;
import org.sandbox.models.DAOFactory;
import org.sandbox.network.MinaIoHandler;
import org.sandbox.network.SessionAttributes;
import org.sandbox.Console;
import org.sandbox.Main;
import org.sandbox.Utils;

public class ServerIoHandler extends MinaIoHandler {
	
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        String HC = Utils.randomIntToString(32);
        SessionAttributes.CONNEXION.setValue(HC, session);
        ServerPacketEnum.HELLO_CONNECTION.send(session, HC);
    }
    
    @Override
    public void sessionClosed(IoSession session){
        Account account = SessionAttributes.ACCOUNT.getValue(session);
        
        if(account != null){
            account.removeSession();
            Console.debug(account.name + " are now offline.");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) {
    	Console.info("send : " + (String) message);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (!Main.running) {
            return;
        }
        String packet = ((String) message).trim();
        if (packet.length() > 0) {
        	Console.info("received : " + (String) packet);

            if (!session.containsAttribute("version")) {
                if (!packet.equals(Main.clientVersion)) {
                    ServerPacketEnum.REQUIRE_VERSION.send(session);
                    session.close(true);
                } else {
                    session.setAttribute("version");
                }
            } else {
            	
                String[] data = packet.split("\n");
                if (data.length == 2) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    Account account = DAOFactory.account().getByName(username);
                    if (account == null || !account.valid(password, SessionAttributes.CONNEXION.getValue(session))) {
                        ServerPacketEnum.LOGIN_ERROR.send(session);
                        session.close(true);
                        return;
                    } else if (account.banned > 0) {
                    	ServerPacketEnum.BANNED.send(session);
                    	session.close(true);
                    	return;
                    }

                    if (account.getSession() == null) {
                        account.setSession(session);
                        ServerPacketEnum.COMMUNITY.send(session, account.community + "");
                        ServerPacketEnum.PSEUDO.send(session, account.nickname);
                        ServerPacketEnum.HOSTS_LIST.send(session);
                        ServerPacketEnum.QUESTION.send(session, account.question);
                        ServerPacketEnum.LOGIN_OK.send(session, account.level > 0 ? "1" : "0");
                    } else {
                        ServerPacketEnum.LOGIN_ALREADY_CONNECTED.send(session);
                        session.close(false);
                    }
                } else {
                    ServerInputHandler.instance().parsePacket(packet, session);
                }
            }
        }
    }
}
