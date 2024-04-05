package com.lucaf.mysqlplayerlink.core.connection;

import com.google.gson.Gson;
import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import com.lucaf.mysqlplayerlink.json.InventorySaveJson;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryHandler {
    static String table = Mysqlplayerlink.config.getMysqlTablePefix() + "inventory";

    public static void checkCreateInventoryTable() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id INT(255) NOT NULL AUTO_INCREMENT," +
                "UUID VARCHAR(255) NOT NULL DEFAULT ''," +
                "name TINYTEXT," +
                "inventory_data LONGTEXT," +
                "is_locked INT(2) DEFAULT '0'," +
                "last_sync DATETIME," +
                "UNIQUE KEY UUID_index_inv (UUID)," +
                "PRIMARY KEY (id)" +
                ") ENGINE=InnoDB;", table);
        Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
    }

    Player player;

    public InventoryHandler(Player player) {
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
            sql = Mysqlplayerlink.mysql.prepareStatement(String.format("INSERT INTO %s (UUID, name, inventory_data, is_locked, last_sync) VALUES (?, ?, ?, ?, ?)", table));
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
            Mysqlplayerlink.log("Failed to login user " + player.getName());
            Mysqlplayerlink.log(e.getMessage());
        }
    }

    public void setLockStatus(boolean status) {
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("UPDATE %s SET is_locked = %s WHERE UUID = ?", table, status ? "1" : "0"));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            sql.setString(1, player.getUniqueId().toString());
            Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to set lock status for player " + player.getName());
            Mysqlplayerlink.log(e.getMessage());
        }
    }

    public void updateInventory(InventorySaveJson save){
        try {
            PreparedStatement sql = Mysqlplayerlink.mysql.prepareStatement(String.format("UPDATE %s SET inventory_data = ?, last_sync = ? WHERE UUID = ?", table));
            if (sql == null) {
                Mysqlplayerlink.log("Failed to prepare statement, connection is null. Is database down?");
                return;
            }
            Gson gson = new Gson();
            sql.setString(1, gson.toJson(save, InventorySaveJson.class));
            sql.setString(2, Mysqlplayerlink.mysql.getCurrentDateTime());
            sql.setString(3, player.getUniqueId().toString());
            Mysqlplayerlink.mysql.runSqlForNoResponse(sql);
        } catch (SQLException e) {
            Mysqlplayerlink.log("Failed to update inventory for player " + player.getName());
            Mysqlplayerlink.log(e.getMessage());
        }
    }

}
