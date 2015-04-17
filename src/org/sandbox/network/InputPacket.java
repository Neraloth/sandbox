package org.sandbox.network;

import org.apache.mina.core.session.IoSession;

public interface InputPacket {
	
    public String id();
    
    public void perform(String extra, IoSession session);
}
