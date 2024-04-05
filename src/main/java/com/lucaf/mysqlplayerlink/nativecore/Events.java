package com.lucaf.mysqlplayerlink.nativecore;

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
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        sendSyncData(event.getPlayer());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        sendSyncData(event.getPlayer());
    }
}
