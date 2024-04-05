package com.lucaf.mysqlplayerlink.json;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter @Setter
public class InventorySaveJson  {
    String inventory;
    int gamemode;
    int slot;
    double health;
    double health_scale;
    double max_health;
    boolean is_health_scale;
    int food;
    float saturation;
    int air;
    int max_air;
    int level;
    float exp;
    int exp_to_level;
    int exp_total;
    String potion_effects;
    public InventorySaveJson(Inventory inventory, int slot, int gamemode,
                             double health, double health_scale, double max_health, boolean is_health_scale,
                             int food, float saturation,
                             int air, int air_max,
                             int level, float exp, int exp_to_level, int exp_total, Collection<PotionEffect> potion_effects) {
        this.inventory = serializeInventory(inventory);
        this.gamemode = gamemode;
        this.slot = slot;
        this.health = health;
        this.food = food;
        this.level = level;
        this.exp = exp;
        this.health_scale = health_scale;
        this.max_health = max_health;
        this.is_health_scale = is_health_scale;
        this.saturation = saturation;
        this.air = air;
        this.max_air = air_max;
        this.exp_to_level = exp_to_level;
        this.exp_total = exp_total;
        this.potion_effects = serializePotionEffects(potion_effects);
    }

    public InventorySaveJson() {
        this.inventory = "";
        this.gamemode = 0;
        this.slot = 0;
        this.health = 0;
        this.food = 0;
        this.level = 0;
        this.exp = 0;
        this.health_scale = 0;
        this.max_health = 0;
        this.is_health_scale = false;
        this.saturation = 0;
        this.air = 0;
        this.max_air = 0;
        this.exp_to_level = 0;
        this.exp_total = 0;
        this.potion_effects = "";
    }
    public String serializePotionEffects(Collection<PotionEffect> potionEffects) {
        try {
            if (potionEffects == null) {
                return "";
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(potionEffects.size());
            for (PotionEffect effect : potionEffects) {
                dataOutput.writeObject(effect);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    //Not sure about this
    public Collection<PotionEffect> deserializePotionEffects(String data) {
        try {
            if (data == null || data.equals("")) {
                return null;
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            Collection<PotionEffect> effects = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                effects.add((PotionEffect) dataInput.readObject());
            }
            return effects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String serializeItemstack(ItemStack[] itemStacks) {
        try {
            if (itemStacks == null) {
                return "";
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStacks.length);
            for (int i = 0; i < itemStacks.length; i++) {
                dataOutput.writeObject(itemStacks[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public ItemStack[] deserializeItemstack(String data) {
        try {
            if (data == null || data.equals("")) {
                return null;
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String serializeInventory(Inventory inventory) {
        try {
            if (inventory == null) {
                return "";
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Inventory deserializeInventory(String data) {
        try {
            if (data == null || data.equals("")) {
                return null;
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = serializeInventory(inventory);
    }
    public ItemStack[] getInventory() {
        return deserializeItemstack(inventory);
    }
    public void setPotionEffects(Collection<PotionEffect> potionEffects) {
        this.potion_effects = serializePotionEffects(potionEffects);
    }
    public Collection<PotionEffect> getPotionEffects() {
        return deserializePotionEffects(potion_effects);
    }
}
