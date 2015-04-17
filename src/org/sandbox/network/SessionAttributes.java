package org.sandbox.network;

import org.apache.mina.core.session.IoSession;
import org.sandbox.character.Account;

@SuppressWarnings("unchecked")
public class SessionAttributes<T> {
	
    static final public SessionAttributes<String> CONNEXION = new SessionAttributes<>();
    static final public SessionAttributes<Account> ACCOUNT = new SessionAttributes<>();
    
    private SessionAttributes() {
    	
    }

	public T getValue(IoSession session) {
        if(session == null)
            return null;
        return (T)session.getAttribute(this);
    }
    
    public void setValue(T value, IoSession session) {
        session.setAttribute(this, value);
    }
    
    public void removeValue(IoSession session) {
        if(session == null) return;
        session.removeAttribute(this);
    }
    
    public boolean exists(IoSession session) {
        if(session == null) return false;
        return session.containsAttribute(this);
    }
}
