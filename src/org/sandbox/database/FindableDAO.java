package org.sandbox.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sandbox.Console;

abstract public class FindableDAO<T extends Model> extends DAO<T> {
    
    final protected Query find = DatabaseHandler.instance().prepareQuery("SELECT * FROM " + tableName() + " WHERE " + primaryKey() + " = ?");

    public T find(int primaryKey) {
        if (primaryKey().isEmpty()) {
            return null;
        }

        ReservedQuery query = find.reserveQuery();
        
        try {
        	query.getStatement().setInt(1, primaryKey);
            ResultSet RS = query.getStatement().executeQuery();

            if (!RS.next()) {
               Console.info("impossible to found the primary key in " + tableName());
               return null;
            }

            return createByResultSet(RS);
        } catch (SQLException e) {
            Console.error("impossible to execute query : " + find);
            return null;
        } finally {
            query.release();
        }
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        String query = "SELECT * FROM " + tableName();
        try {
            ResultSet RS = DatabaseHandler.instance().executeQuery(query);
            while (RS.next()) {
                list.add(createByResultSet(RS));
            }
        } catch (SQLException ex) {
            Console.error("impossible to execute query : " + query);
        }
        return list;
    }
}
