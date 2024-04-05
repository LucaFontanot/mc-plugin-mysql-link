package com.lucaf.mysqlplayerlink.nativecore;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    public void sendSyncData(Player player){
        InventoryManager inventoryManager = new InventoryManager(player);
        inventoryManager.syncInventoryToRemote();
    }
    public void getSyncData(Player player){
        InventoryManager inventoryManager = new InventoryManager(player);
        inventoryManager.syncInventoryFromRemote();
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (Mysqlplayerlink.config.getConfig_type_inventories().equals("sync-down") || Mysqlplayerlink.config.getConfig_type_inventories().equals("sync")){
            getSyncData(event.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if (Mysqlplayerlink.config.getConfig_type_inventories().equals("sync-up") || Mysqlplayerlink.config.getConfig_type_inventories().equals("sync")){
            sendSyncData(event.getPlayer());
        }
    }
}
