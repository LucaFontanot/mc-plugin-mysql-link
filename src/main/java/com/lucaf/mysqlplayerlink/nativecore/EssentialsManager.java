package com.lucaf.mysqlplayerlink.nativecore;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import com.lucaf.mysqlplayerlink.core.connection.EssentialsHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.bukkit.Bukkit.getServer;

public class EssentialsManager {
    Player player;
    EssentialsHandler essentialsHandler;
    public EssentialsManager(Player player) {
        this.player = player;
        essentialsHandler = new EssentialsHandler(player);
    }

    public void syncEssentialsFromRemote() {
    }
    public void syncEssentialsToRemote() {
        File playerData = new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder(), Paths.get("userdata",player.getUniqueId().toString()).toString() + ".yml");
        if (playerData.exists()) {
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerData);
            String essentialsData = playerConfig.saveToString();
            essentialsHandler.updateEssentialsData(essentialsData);
        } else {
            Mysqlplayerlink.log("Essentials data does not exist for player: " + player.getName());
        }


    }
}
