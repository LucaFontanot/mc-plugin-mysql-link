package com.lucaf.mysqlplayerlink.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
@Setter
public class ConfigHandler {
    public String mysqlAddress;
    public String mysqlPort;
    public String mysqlDatabase;
    public String mysqlUsername;
    public String mysqlPassword;
    public String driver;
    public String mysqlTablePefix;
    public String serverName;
    public String config_type_inventories;
    public ConfigHandler(FileConfiguration config) {
        config.options().copyDefaults(true);
        mysqlAddress = config.getString("mysql.address");
        mysqlPort = config.getString("mysql.port");
        mysqlDatabase = config.getString("mysql.database");
        mysqlUsername = config.getString("mysql.username");
        mysqlPassword = config.getString("mysql.password");
        driver = config.getString("mysql.driver");
        mysqlTablePefix = config.getString("mysql.tablePrefix");
        serverName = config.getString("serverName");
        config_type_inventories = config.getString("sync.inventory");
    }

}
