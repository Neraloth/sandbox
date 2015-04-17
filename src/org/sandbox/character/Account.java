package org.sandbox.character;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.sandbox.Cipher;
import org.sandbox.database.Model;
import org.sandbox.models.DAOFactory;
import org.sandbox.network.SessionAttributes;
import org.sandbox.network.Sessionable;
import org.sandbox.Utils;

public class Account implements Model, Sessionable {

	public int id;
	public String name;
	public String password;
	public String nickname;
	public String question;
	public String answer;
	public int level;
	public byte community;
	public byte banned = 0;
	
	protected HashMap<Integer, Character> characters = null;
	private IoSession session;
	private String ip;
	
    static final private ConcurrentHashMap<String, Account> pendingAccounts = new ConcurrentHashMap<>();
    private String waitingTicket = null;
    private Character waitingCharacter = null;
    
    public HashMap<Integer, Character> getCharacters() {
        if (characters == null) {
            characters = DAOFactory.character().getByAccountId(id);
        }
        return characters;
    }
    
    public void addCharacter(Character character) {
        getCharacters().put(character.id, character);
    }
    
    public Character getCharacter(int id) {
        return getCharacters().get(id);
    }
    
    public void deleteCharacter(int id) {
        DAOFactory.character().delete(characters.remove(id));
    }
    
    public void setWaitingCharacter(Character character) {
        waitingCharacter = character;
    }
    
    public Character getWaitingCharacter() {
        Character character = waitingCharacter;
        waitingCharacter = null;
        return character;
    }
    
    public String setWaiting() {
        waitingTicket = Utils.randomIntToString(32);
        pendingAccounts.put(waitingTicket, this);
        
        return waitingTicket;
    }
    
    public static Account getWaitingAccount(String ticket) {
        return pendingAccounts.get(ticket);
    }

    public String getIp() {
        return ip;
    }

    public boolean isWaiting(String ip) {
        return waitingTicket != null;
    }
    
    public void removeWaiting() {
        pendingAccounts.remove(waitingTicket);
        waitingTicket = null;
    }
    
    public boolean valid(String password, String key) {
        String cryptPass = Cipher.encode(this.password, key);
        return password.equals(cryptPass);
    }
    
    public void setSession(IoSession session) {
        this.session = session;
        SessionAttributes.ACCOUNT.setValue(this, session);
        InetSocketAddress ISA = (InetSocketAddress)session.getRemoteAddress();
        ip = ISA.getAddress().getHostAddress();
    }

    public void removeSession() {
        session = null;
    }
    
	@Override
	public IoSession getSession() {
		return session;
	}

	@Override
	public int getPrimaryKey() {
		return id;
	}

	@Override
	public void clear() {
        id = 0;
        name = null;
        password = null;
        nickname = null;
        question = null;
        answer = null;
        level = 0;
        community = 0;
	}
}
