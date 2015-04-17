package org.sandbox.database;

import org.sandbox.Console;

public class Query {
	
    final private String sql;
    final private DatabaseHandler handler;

    public Query(String sql, DatabaseHandler handler) {
        this.sql = sql;
        this.handler = handler;
    }

    public String getSql() {
        return sql;
    }

    public DatabaseHandler getHandler() {
        return handler;
    }
    
    public ReservedQuery reserveQuery() {
    	Console.debug("Reserving query for " + toString());
        DatabaseConnection connexion = handler.getFreeConnection();
        return new ReservedQuery(this, connexion.getPreparedStatement(this), connexion);
    }

    @Override
    public String toString() {
        return "Query{" + "sql=" + sql + '}';
    }
    
}
