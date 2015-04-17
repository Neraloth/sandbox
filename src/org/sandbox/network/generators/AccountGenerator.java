package org.sandbox.network.generators;

import org.sandbox.Main;
import org.sandbox.character.Account;
import org.sandbox.character.Character;

public class AccountGenerator {
	
    public String generateCharactersList(Account account){
        if(account.getCharacters().isEmpty())
            return "0";
        
        StringBuilder sb = new StringBuilder(160 * account.getCharacters().size());
        sb.append(account.getCharacters().size());
        
        for(Character characters : account.getCharacters().values()){
            sb.append('|');
            sb.append(characters.id).append(';');
            sb.append(characters.nickname).append(';');
            sb.append(characters.level).append(';');
            sb.append(characters.skin).append(';');
            sb.append((characters.color1 != -1 ? Integer.toHexString(characters.color1) : "-1")).append(';');
            sb.append((characters.color2 != -1 ? Integer.toHexString(characters.color2) : "-1")).append(';');
            sb.append((characters.color3 != -1 ? Integer.toHexString(characters.color3) : "-1")).append(';');
            sb.append(0).append(';');//generateAccessories(characters)
            sb.append(0).append(';');
            sb.append(Main.serverId + ";");//ServerID
            sb.append(';');//deathCount
            sb.append(';');//LevelMax
        }
        
        return sb.toString();
    }
    
    public String generateServerList(long premiumTime, byte serverID, int nbChar){
        return premiumTime + "|" + serverID + "," + nbChar;
    }
}
