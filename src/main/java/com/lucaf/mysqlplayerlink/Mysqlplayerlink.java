package com.lucaf.mysqlplayerlink;

import com.lucaf.mysqlplayerlink.core.ConfigHandler;
import com.lucaf.mysqlplayerlink.core.connection.InventoryHandler;
import com.lucaf.mysqlplayerlink.core.connection.MySql;
import com.lucaf.mysqlplayerlink.nativecore.Events;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Mysqlplayerlink extends JavaPlugin {
    public static ConfigHandler config;
    public static MySql mysql;
    public static Mysqlplayerlink plugin;

    public void checkMysqlTable(){
        InventoryHandler.checkCreateInventoryTable();
    }
    public static void log(String message){
        plugin.getLogger().info(message);
    }
    @Override
    public void onEnable() {
        plugin = this;
        config = new ConfigHandler(getConfig());
        saveDefaultConfig();
        mysql = new MySql();
        if (mysql.connect()){
            checkMysqlTable();
            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new Events(), this);
        } else {
            getLogger().log(Level.SEVERE,"Failed to connect to the database");
        }

    }

    @Override
    public void onDisable() {
        mysql.closeConnection();
    }
}
