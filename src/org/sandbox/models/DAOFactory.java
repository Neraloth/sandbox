package org.sandbox.models;



public class DAOFactory {

    private static AccountDAO account = null;
    private static CharacterDAO player = null;

    public static AccountDAO account() {
        return account;
    }

    public static CharacterDAO character() {
        return player;
    }

    public static void init() {
        account = new AccountDAO();
        player = new CharacterDAO();
    }
}
