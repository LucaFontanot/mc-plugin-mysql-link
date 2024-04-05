package com.lucaf.mysqlplayerlink.core.connection;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySql {
    private Connection connection = null;

    public MySql() {}

    public boolean connect() {
        try {
            Class.forName(Mysqlplayerlink.config.getDriver());
            connection = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://" + Mysqlplayerlink.config.getMysqlAddress() +
                            ":" + Mysqlplayerlink.config.getMysqlPort() +
                            "/" + Mysqlplayerlink.config.getMysqlDatabase(),
                    Mysqlplayerlink.config.getMysqlUsername(),
                    Mysqlplayerlink.config.getMysqlPassword()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection = null;
    }
    public boolean check() throws SQLException {
        if (connection == null){
            return false;
        }
        if (connection.isClosed()){
            return false;
        }
        if (!connection.isValid(5)){
            return false;
        }
        return true;
    }
    public boolean runSqlForNoResponse(String sql) {
        try {
            if (!check()){
                this.connect();
                return false;
            }
            connection.createStatement().execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public PreparedStatement prepareStatement(String sql) {
        try {
            if (!check()) {
                this.connect();
                return null;
            }
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean runSqlForNoResponse(PreparedStatement statement) {
        try {
            if (!check()) {
                this.connect();
                return false;
            }
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ResultSet runSqlForResponse(String sql) {
        try {
            if (!check()){
                this.connect();
                return null;
            }
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet runSqlForResponse(PreparedStatement statement) {
        try {
            if (!check()) {
                this.connect();
                return null;
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCurrentDateTime() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
}
