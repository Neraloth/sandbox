package org.sandbox.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sandbox.Console;
import org.sandbox.character.Account;
import org.sandbox.database.DatabaseHandler;
import org.sandbox.database.Query;
import org.sandbox.database.ReservedQuery;
import org.sandbox.database.UpdatableDAO;

public class AccountDAO extends UpdatableDAO<Account> {

    private final Map<String, Account> accountsByName = new ConcurrentHashMap<>();
    private final Map<Integer, Account> accountsById = new ConcurrentHashMap<>();
    
    final private Query getByName = DatabaseHandler.instance().prepareQuery("SELECT * FROM accounts WHERE name = ?");
    
    public Account getByName(String name) {
        if (!accountsByName.containsKey(name.toLowerCase())) {
            ReservedQuery query = getByName.reserveQuery();
            try {
                    query.getStatement().setString(1, name);
                    ResultSet RS = query.getStatement().executeQuery();
                    if (RS.next()) {
                        return createByResultSet(RS);
                    } else {
                        return null;
                    }
            }catch (SQLException ex) {
            	Console.error("impossible to load account : " + name);
                return null;
            }finally{
                query.release();
            }
        }
        return accountsByName.get(name);
    }

    public Account getById(int id) {
        if (!accountsById.containsKey(id)) {
            return find(id);
        }
        return accountsById.get(id);
    }

    @Override
    protected String tableName() {
        return "accounts";
    }

    @Override
    protected Account createByResultSet(ResultSet RS) {
        try {
            Account account = new Account();

            account.id = RS.getInt("id");
            account.name = RS.getString("name");
            account.password = RS.getString("password");
            account.nickname = RS.getString("nickname");
            account.question = RS.getString("question");
            account.answer = RS.getString("answer");
            account.level = RS.getInt("level");
            account.community = RS.getByte("community");
            account.banned = RS.getByte("banned");
            
            accountsByName.put(RS.getString("name").toLowerCase(), account);
            accountsById.put(RS.getInt("id"), account);

            return account;
        } catch (SQLException e) {
        	Console.error("impossible to load accounts");
            return null;
        }
    }
    
    @Override
    public boolean update(Account obj) {
    	Console.error("not supported yet");
		return false;
    }

    @Override
    public boolean create(Account obj) {
    	Console.error("not supported yet");
		return false;
    }
}
