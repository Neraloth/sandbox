package org.sandbox.network;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.sandbox.Console;
import org.sandbox.character.Account;

public abstract class MinaIoHandler extends IoHandlerAdapter {

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    	Console.info("received : " + (String) message);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        Console.success("new connection");
    }

    @Override
    public final void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	Console.error(cause.getMessage());
        session.close(true);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    	Console.info("send : " + (String) message);
    }

    protected static Account getAccount(IoSession session) {
        Account account = (Account) session.getAttribute("account");
        return account;
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Account account = getAccount(session);
        String name = "?";
        if (account != null) {
            name = account.name;
        }
        Console.debug("Deconnection for inactivity of " + name);
        session.write("M01|");
        session.close(true);
    }
}
