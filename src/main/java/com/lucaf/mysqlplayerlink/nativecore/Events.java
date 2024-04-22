package com.lucaf.mysqlplayerlink.nativecore;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            if (Mysqlplayerlink.config.getConfig_type_inventories().equals("sync-down") || Mysqlplayerlink.config.getConfig_type_inventories().equals("sync")) {
                InventoryManager inventoryManager = new InventoryManager(player);
                //inventoryManager.syncInventoryFromRemote();
            } else {
                InventoryManager inventoryManager = new InventoryManager(player);
                inventoryManager.syncInventoryToRemote();
            }
            if (Mysqlplayerlink.hasEssentials && (Mysqlplayerlink.config.getConfig_type_essentials().equals("sync") || Mysqlplayerlink.config.getConfig_type_essentials().equals("sync-down"))) {
                EssentialsManager essentialsManager = new EssentialsManager(event.getPlayer());
                //essentialsManager.syncEssentialsFromRemote();
            } else if(Mysqlplayerlink.hasEssentials) {
                EssentialsManager essentialsManager = new EssentialsManager(event.getPlayer());
                essentialsManager.syncEssentialsToRemote();
            }
        } catch (Exception e) {
            Mysqlplayerlink.log("Failed to sync data for player: " + event.getPlayer().getName());
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            Player player = event.getPlayer();

            if (Mysqlplayerlink.config.getConfig_type_inventories().equals("sync-up") || Mysqlplayerlink.config.getConfig_type_inventories().equals("sync")) {
                InventoryManager inventoryManager = new InventoryManager(player);
                inventoryManager.syncInventoryToRemote();
            }
            if (Mysqlplayerlink.hasEssentials && (Mysqlplayerlink.config.getConfig_type_essentials().equals("sync") || Mysqlplayerlink.config.getConfig_type_essentials().equals("sync-up"))) {
                EssentialsManager essentialsManager = new EssentialsManager(event.getPlayer());
                essentialsManager.syncEssentialsToRemote();
            }
        } catch (Exception e) {
            Mysqlplayerlink.log("Failed to sync data for player: " + event.getPlayer().getName());
            e.printStackTrace();
        }
    }
}
