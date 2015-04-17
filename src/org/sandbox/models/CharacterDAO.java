package org.sandbox.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.sandbox.Console;
import org.sandbox.database.DatabaseHandler;
import org.sandbox.database.Query;
import org.sandbox.database.ReservedQuery;
import org.sandbox.database.UpdatableDAO;
import org.sandbox.character.Character;

public class CharacterDAO extends UpdatableDAO<Character> {

    final private Query findByName = DatabaseHandler.instance().prepareQuery("SELECT * FROM characters WHERE nickname = ?");
    final private Query getByAccountId = DatabaseHandler.instance().prepareQuery("SELECT * FROM characters WHERE account = ?");
    final private Query create = DatabaseHandler.instance().prepareInsert("INSERT INTO characters(account, nickname, breed, gender, color1, color2, color3, skin, level, experience, mapId, cellId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
    final private Query countName = DatabaseHandler.instance().prepareQuery("SELECT COUNT(*) AS count FROM characters WHERE nickname = ?");
    final private Query countByAccount = DatabaseHandler.instance().prepareQuery("SELECT COUNT(*) AS count FROM characters WHERE account = ?");
    final private Query update = DatabaseHandler.instance().prepareQuery("UPDATE characters SET level = ?, experience = ?, skin = ?, mapId = ?, cellId = ?, orientation = ?, money = ? WHERE id = ?");
    final private ConcurrentHashMap<Integer, Character> charactersById = new ConcurrentHashMap<>();

	@Override
	public boolean update(Character character) {
        ReservedQuery query = update.reserveQuery();
        try {
            int i = 0;
            query.getStatement().setShort(++i, character.level);
            query.getStatement().setLong(++i, character.experience);
            query.getStatement().setInt(++i, character.skin);
            query.getStatement().setShort(++i, character.mapId);
            query.getStatement().setShort(++i, character.cellId);
            query.getStatement().setByte(++i, character.orientation);
            query.getStatement().setLong(++i, character.money);
            
            query.getStatement().setInt(++i, character.id);

            return query.getStatement().execute();
            
        } catch (SQLException e) {
        	Console.error("impossible to save character : " + character);
            return false;
            
        } finally {
            query.release();
        }
	}

	@Override
	public boolean create(Character character) {
        ReservedQuery query = create.reserveQuery();
        try {
            query.getStatement().setInt(1, character.accountId);
            query.getStatement().setString(2, character.nickname);
            query.getStatement().setShort(3, character.breed);
            query.getStatement().setShort(4, character.gender);
            query.getStatement().setInt(5, character.color1);
            query.getStatement().setInt(6, character.color2);
            query.getStatement().setInt(7, character.color3);
            query.getStatement().setInt(8, character.skin);
            query.getStatement().setShort(9, character.level);
            query.getStatement().setLong(10, character.experience);
            query.getStatement().setShort(11, character.mapId);
            query.getStatement().setShort(12, character.cellId);

            query.getStatement().executeUpdate();

            ResultSet RS = query.getStatement().getGeneratedKeys();

            int id = 0;
            if (RS.next()) {
                id = RS.getInt(1);
            } if (id == 0) {
                return false;
            } else {
                character.id = id;
            }
            
        } catch(SQLException ex) {
        	Console.error("impossible to create character");
            return false;
        } finally {
            query.release();
        }
        
        return true;
	}

	@Override
	protected String tableName() {
		return "characters";
	}

	@Override
	protected Character createByResultSet(ResultSet RS) {
        try {
            Character character = new Character();

            character.id = RS.getInt("id");
            character.nickname = RS.getString("nickname");
            character.accountId = RS.getInt("account");
            character.breed = RS.getByte("breed");
            character.color1 = RS.getInt("color1");
            character.color2 = RS.getInt("color2");
            character.color3 = RS.getInt("color3");
            character.skin = RS.getShort("skin");
            character.gender = RS.getByte("gender");
            character.level = RS.getShort("level");
            character.experience = RS.getLong("experience");
            character.mapId = RS.getShort("mapId");
            character.cellId = RS.getShort("cellId");
            character.orientation = RS.getByte("orientation");
            character.money = RS.getInt("money");

            charactersById.put(character.id, character);

            return character;
        } catch (SQLException e) {
            Console.error("impossible to create character by createByResultSet function");
            return null;
        }
	}

    public Character findByName(String name) {
        ReservedQuery query = findByName.reserveQuery();
        
        try {
            query.getStatement().setString(1, name);
            ResultSet RS = query.getStatement().executeQuery();

            if (!RS.next()) {
                return null;
            }
            return createByResultSet(RS);
            
        } catch (SQLException ex) {
        	Console.error("impossible to load character by findByName function");
            return null;
        } finally {
            query.release();
        }
    }

    public Character getById(int id) {
        if (!charactersById.containsKey(id)) {
            return find(id);
        }
        return charactersById.get(id);
    }

    public HashMap<Integer, Character> getByAccountId(int accountId) {
        HashMap<Integer, Character> players = new HashMap<>();

        ReservedQuery query = getByAccountId.reserveQuery();
        try {
            query.getStatement().setInt(1, accountId);
            ResultSet RS = query.getStatement().executeQuery();

            while (RS.next()) {
                Character c = createByResultSet(RS);
                if (c != null) {
                    players.put(c.id, c);
                }
            }
        } catch (SQLException e) {
        	Console.error("impossible to load character list for account : " + accountId);
        } finally {
            query.release();
        }

        return players;
    }

    public boolean nameExists(String name) {
        ReservedQuery query = countName.reserveQuery();
        try {
            query.getStatement().setString(1, name);
            ResultSet RS = query.getStatement().executeQuery();
            
            if (!RS.next()) {
                return true;
            }

            return RS.getInt("count") > 0;
        } catch (SQLException e) {
        	Console.error("impossible to know if the name is exist : " + name);
            return true;
        } finally {
            query.release();
        }
    }

    public int countByAccount(int accountId) {
        ReservedQuery query = countByAccount.reserveQuery();
        try {
            query.getStatement().setInt(1, accountId);
            ResultSet RS = query.getStatement().executeQuery();
            if (!RS.next()) {
                return 0;
            }
            return RS.getInt("count");
        } catch (SQLException e) {
        	Console.error("impossible to count characters for account : " + accountId);
            return 0;
        } finally {
            query.release();
        }
    }
}
