package org.sandbox.database;

import java.sql.SQLException;

import org.sandbox.Console;

abstract public class CreatableDAO<T extends Model> extends FindableDAO<T> {
	
    final protected Query delete = DatabaseHandler.instance().prepareQuery("DELETE FROM " + tableName() + " WHERE " + primaryKey() + " = ?");

    abstract public boolean create(T obj);

    public boolean delete(int primaryKey) {
        ReservedQuery query = delete.reserveQuery();
        try {
            if (primaryKey().isEmpty()) {
                return false;
            }
            query.getStatement().setInt(1, primaryKey);
            return query.getStatement().execute();
        } catch (SQLException e) {
        	Console.error("impossible to execute query : " + delete);
            return false;
        } finally {
            query.release();
        }
    }

    public boolean delete(T obj) {
        if (delete(obj.getPrimaryKey())) {
            obj.clear();
            return true;
        } else {
            return false;
        }
    }
}
