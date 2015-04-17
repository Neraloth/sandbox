package org.sandbox.network;

import org.sandbox.network.generators.AccountGenerator;
import org.sandbox.network.generators.CharacterGenerator;

public class GeneratorsRegistry {
	
    private static AccountGenerator account = null;
    private static CharacterGenerator character = null;

    public static AccountGenerator getAccount() {
        if(account == null)
            account = new AccountGenerator();
        return account;
    }

    public static void setAccount(AccountGenerator account) {
        GeneratorsRegistry.account = account;
    }
    
    public static CharacterGenerator getCharacter() {
        if(character == null)
            character = new CharacterGenerator();
        return character;
    }

    public static void setCharacter(CharacterGenerator character) {
        GeneratorsRegistry.character = character;
    }
}