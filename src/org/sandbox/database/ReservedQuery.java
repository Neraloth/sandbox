package org.sandbox.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sandbox.Console;

public class ReservedQuery {
	
    final private Query query;
    final private PreparedStatement statement;
    final private DatabaseConnection connection;
    final private long startTime = System.currentTimeMillis();

    public ReservedQuery(Query query, PreparedStatement statement, DatabaseConnection connection) {
        this.query = query;
        this.statement = statement;
        this.connection = connection;
    }

    public Query getQuery() {
        return query;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public DatabaseConnection getConnection() {
        return connection;
    }
    
    public void release() {
        try {
            statement.clearParameters();
        } catch (SQLException ex) {}
        query.getHandler().returnConnection(connection);
        long time = System.currentTimeMillis() - startTime;
        Console.debug(query + " releasing after " + time + " milliseconds");
    }
}