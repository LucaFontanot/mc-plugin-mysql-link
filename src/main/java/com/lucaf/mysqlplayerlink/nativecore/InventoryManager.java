package com.lucaf.mysqlplayerlink.nativecore;

import com.lucaf.mysqlplayerlink.Mysqlplayerlink;
import com.lucaf.mysqlplayerlink.core.connection.InventoryHandler;
import com.lucaf.mysqlplayerlink.json.InventorySaveJson;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class InventoryManager {
    private Player player;
    private InventoryHandler inventoryHandler;
    public InventoryManager(Player player) {
        this.player = player;
        this.inventoryHandler = new InventoryHandler(player);
    }
    public void syncInventoryFromRemote(){
        //Get local inventory last update time
        if (inventoryHandler.isLocked()){
            return;
        }
        inventoryHandler.setLockStatus(true);
        InventorySaveJson inventorySaveJson = inventoryHandler.getInventory();
        if (inventorySaveJson == null){
            return;
        }
        player.getInventory().setContents(inventorySaveJson.getInventory());
        player.getInventory().setHeldItemSlot(inventorySaveJson.getSlot());
        player.setGameMode(GameMode.getByValue(inventorySaveJson.getGamemode()));
        player.setHealth(inventorySaveJson.getHealth());
        if (inventorySaveJson.is_health_scale){
            player.setHealthScaled(true);
            player.setHealthScale(inventorySaveJson.getHealth_scale());
        } else {
            player.setHealthScaled(false);
        }
        player.setFoodLevel(inventorySaveJson.getFood());
        player.setSaturation(inventorySaveJson.getSaturation());
        player.setRemainingAir(inventorySaveJson.getAir());
        player.setMaximumAir(inventorySaveJson.getMax_air());
        player.setLevel(inventorySaveJson.getLevel());
        player.setExp(inventorySaveJson.getExp());
        player.setTotalExperience(inventorySaveJson.getExp_total());
        Collection<PotionEffect> potionEffects = inventorySaveJson.getPotionEffects();
        for (PotionEffect potionEffect : player.getActivePotionEffects()){
            player.removePotionEffect(potionEffect.getType());
        }
        for (PotionEffect potionEffect : potionEffects){
            Mysqlplayerlink.log("Adding potion effect: " + potionEffect.getType().getName() + " " + potionEffect.getAmplifier() + " " + potionEffect.getDuration());
            player.addPotionEffect(potionEffect);
        }

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
                player.getActivePotionEffects(),
                player.getLocation()
        );
        inventoryHandler.updateInventory(inventorySaveJson);
        inventoryHandler.setLockStatus(false);
    }

}
