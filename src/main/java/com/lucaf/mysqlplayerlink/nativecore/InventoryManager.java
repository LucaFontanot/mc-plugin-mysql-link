package com.lucaf.mysqlplayerlink.nativecore;

import com.lucaf.mysqlplayerlink.core.connection.InventoryHandler;
import com.lucaf.mysqlplayerlink.json.InventorySaveJson;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class InventoryManager {
    private Player player;
    private InventoryHandler inventoryHandler;
    public InventoryManager(Player player) {
        this.player = player;
        this.inventoryHandler = new InventoryHandler(player);
    }
    public void syncInventoryToRemote(){
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        //Fix potion effect health boost. This will be managed by the potion effect handler
        if (maxHealth>20d && player.getPotionEffect(PotionEffectType.HEALTH_BOOST)!=null){
            maxHealth = maxHealth - 4 * (player.getPotionEffect(PotionEffectType.HEALTH_BOOST).getAmplifier() + 1);
        }

        InventorySaveJson inventorySaveJson = new InventorySaveJson(
                player.getInventory(),
                player.getInventory().getHeldItemSlot(),
                player.getGameMode().getValue(),

                Math.min(player.getHealth(), maxHealth),
                player.isHealthScaled() ? player.getHealthScale() : 0,
                maxHealth,
                player.isHealthScaled(),

                player.getFoodLevel(),
                player.getSaturation(),

                player.getRemainingAir(),
                player.getMaximumAir(),

                player.getLevel(),
                player.getExp(),
                player.getExpToLevel(),
                player.getTotalExperience(),
                player.getActivePotionEffects()
        );
        inventoryHandler.updateInventory(inventorySaveJson);
    }

}
