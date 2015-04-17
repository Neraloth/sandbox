package org.sandbox.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {

    final private Connection connection;
    final private Map<Query, PreparedStatement> statements = new HashMap<>();

    public DatabaseConnection(String host, String user, String password, String name) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + name, user, password);
        connection.setAutoCommit(true);
    }
    
    synchronized public ResultSet query(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }
    
    synchronized public void prepare(Query query) throws SQLException {
        statements.put(query, connection.prepareStatement(query.getSql()));
    }
    
    synchronized public void prepareInsert(Query query) throws SQLException {
        statements.put(query, connection.prepareStatement(query.getSql(), PreparedStatement.RETURN_GENERATED_KEYS));
    }
    
    synchronized public PreparedStatement getPreparedStatement(Query query) {
        return statements.get(query);
    }
    
    synchronized public void close() {
        try {
            for(PreparedStatement statement : statements.values()) {
                statement.close();
            }
            statements.clear();

            connection.close();
        } catch(SQLException e) { }
    }
}
