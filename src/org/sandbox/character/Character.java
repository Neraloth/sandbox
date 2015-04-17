package org.sandbox.character;

import org.apache.mina.core.session.IoSession;
import org.sandbox.database.Model;
import org.sandbox.network.Sessionable;

public class Character implements Model, Sessionable {

	private IoSession session = null;
	
	public int id;
	public int accountId;
	public String nickname;
	public short breed;
	public byte gender;
	public int color1;
	public int color2;
	public int color3;
	public int skin;
	public int size;
	public short level = 1;
	public long experience;
	public long money;
	public byte orientation;
	public short mapId;
	public short cellId;
	public short lastMapId;
	public short lastCellId;
	
	@Override
	public int getPrimaryKey() {
		return id;
	}

	@Override
	public void clear() {
		id = 0;
	}

    @Override
    public String toString() {
        return "Character{" + "id=" + id + ", nickname=" + nickname + ", account=" + accountId + ", level=" + level + ", experience=" + experience + ", color1=" + color1 + ", color2=" + color2 + ", color3=" + color3 + ", skin=" + skin + ", gender=" + gender + ", breed=" + breed + ", mapId=" + mapId + ", cellId=" + cellId + ", orientation=" + orientation + ", money=" + money + '}';
    }

	@Override
    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }
}
