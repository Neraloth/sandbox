package org.sandbox.network;

import org.apache.mina.core.session.IoSession;

public interface Sessionable {
	
    public IoSession getSession();
    
}
