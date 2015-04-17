package org.sandbox.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.sandbox.Console;
import org.sandbox.Main;

public class DatabaseHandler {
	
    final private Set<DatabaseConnection> connections = new HashSet<>();
    final private ArrayBlockingQueue<DatabaseConnection> pool;
    final private int POOL_SIZE;
    
    static private DatabaseHandler instance = null;

    private DatabaseHandler(String host, String user, String pass, String name, int poolSize) throws SQLException {
        POOL_SIZE = poolSize;
        pool = new ArrayBlockingQueue<>(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; ++i) {
            DatabaseConnection connection = new DatabaseConnection(host, user, pass, name);
            pool.add(connection);
            connections.add(connection);
        }
    }
    
    DatabaseConnection getFreeConnection() {
        try {
            return pool.take();
        } catch (InterruptedException ex) {
            return null;
        }
    }
    
    void returnConnection(DatabaseConnection connection) {
        pool.add(connection);
    }
    
    synchronized public Query prepareQuery(String sql) {
         Query query = new Query(sql, this);
         
         for(DatabaseConnection connection : connections) {
             try {
                 connection.prepare(query);
             } catch (SQLException ex) {
            	 Console.error("impossible to prepare query " + query);
                 System.exit(1);
             }
         }
         
         return query;
    }
    
    synchronized public Query prepareInsert(String sql) {
         Query query = new Query(sql, this);
         
         for(DatabaseConnection connection : connections) {
             try {
                 connection.prepareInsert(query);
             } catch (SQLException ex) {
            	 Console.error("impossible to prepare query " + query);
                 System.exit(1);
             }
         }
         
         return query;
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            DatabaseConnection connection = pool.take();
            try {
                return connection.query(sql);
            } finally {
                pool.add(connection);
            }
        } catch (InterruptedException ex) {
            return null;
        }
    }
        
    static public void init() {
        if(instance != null) {
            Console.info("DatabaseHandler already initialized !");
            return;
        } try {
            instance = new DatabaseHandler(
                    Main.database_host,
                    Main.database_user,
                    Main.database_password,
                    Main.database_name,
                    8 //Pool size
            );
        } catch(SQLException ex) {
        	Console.error("connection impossible with database : " + ex);
            System.exit(1);
        }
        Console.success("connected to database!");
    }
    
    static public DatabaseHandler instance() {
        return instance;
    }
    
    synchronized public void close() {
        for(DatabaseConnection connection : connections) {
            connection.close();
        }
    }
}
