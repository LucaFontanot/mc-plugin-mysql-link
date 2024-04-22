package com.lucaf.mysqlplayerlink.core.connection;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class EssentialsHandler {
    static String table = Mysqlplayerlink.config.getMysqlTablePefix() + "essentials";
    public static void checkCreateInventoryTable() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id INT(255) NOT NULL AUTO_INCREMENT," +
                "UUID VARCHAR(255) NOT NULL DEFAULT ''," +
                "name TINYTEXT," +
                "essentials_data LONGTEXT," +
                "is_locked INT(2) DEFAULT '0'," +
                "last_sync DATETIME," +
                "server_id TINYTEXT," +
                "UNIQUE KEY UUID_index_ess (UUID)," +
                "PRIMARY KEY (id)" +
                ") ENGINE=InnoDB;", table);
        Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
    }
    public static boolean doesEssentialPluginExists(){
        return getServer().getPluginManager().getPlugin("EssentialsX") != null;
    }
    Player player;
    public EssentialsHandler(Player player) {
        this.player = player;
        this.loginUser();
    }

    public void loginUser() {
        //Check if user exists, or create user
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("SELECT name FROM %s WHERE UUID = ?", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            sql.setString(1, player.getUniqueId().toString());
            ResultSet result = Mysqlplayerlink.mysql.runSqlForResponse(sql);
            if (result != null && result.next()){
                return;
            }
            //User does not exist, create user
            sql = Mysqlplayerlink.mysql.prepareStatement(String.format("INSERT INTO %s (UUID, name, essentials_data, is_locked, last_sync) VALUES (?, ?, ?, ?, ?)", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, player.getName());
            sql.setString(3, "");
            sql.setInt(4, 0);
            sql.setString(5, Mysqlplayerlink.mysql.getCurrentDateTime());
            Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to create user in database");
            e.printStackTrace();
        }
    }

    public void setLockStatus(boolean status) {
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("UPDATE %s SET is_locked = ? WHERE UUID = ?", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            sql.setInt(1, status ? 1 : 0);
            sql.setString(2, player.getUniqueId().toString());
            Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to set lock status for user " + player.getName());
            e.printStackTrace();
        }
    }
    public boolean isLocked() {
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("SELECT is_locked FROM %s WHERE UUID = ?", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return false;
            }
            sql.setString(1, player.getUniqueId().toString());
            ResultSet result = Mysqlplayerlink.mysql.runSqlForResponse(sql);
            if (result != null && result.next()) {
                return result.getInt("is_locked") == 1;
            }
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to get lock status for user " + player.getName());
            e.printStackTrace();
        }
        return true;
    }
    public void updateEssentialsData(String data){
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("UPDATE %s SET essentials_data = ?, last_sync = ?, server_id = ? WHERE UUID = ?", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            sql.setString(1, data);
            sql.setString(2, Mysqlplayerlink.mysql.getCurrentDateTime());
            sql.setString(3, Mysqlplayerlink.config.getServerName());
            sql.setString(4, player.getUniqueId().toString());
            Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to update essentials data for user " + player.getName());
            e.printStackTrace();
        }
    }


}
