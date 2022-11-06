package me.yiyi1234.skinticket.manager;

import me.yiyi1234.skinticket.SkinTicket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager {
    public static HashMap<String, ItemStack> itemStackHashMap = new HashMap<>();
    public static List<String> itemStacksStringList = new ArrayList<>();
    public static ItemStack skinRemover;
    NamespacedKey namespacedKey = new NamespacedKey(SkinTicket.getInstance(), "skin");
    public void itemList() {
        int loadAmount = 0;
        if (SkinTicket.getInstance().getConfig().getConfigurationSection("CustomSkin") != null) {

            Set<String> skinStringList = SkinTicket.getInstance().getConfig().getConfigurationSection("CustomSkin").getKeys(false);

            for (String s : skinStringList) {
                Material itemMaterial = Material.getMaterial(SkinTicket.getInstance().getConfig().getString("CustomSkin." + s + ".displayMaterial").toUpperCase(Locale.ROOT));
                ItemStack itemStack = new ItemStack(itemMaterial);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("CustomSkin." + s + ".displayName")));
                itemMeta.setCustomModelData(SkinTicket.getInstance().getConfig().getInt("CustomSkin." + s + ".customModelData"));

                List<String> lore = new ArrayList<>();
                for (String l : SkinTicket.getInstance().getConfig().getStringList("CustomSkin." + s + ".displayLore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', l));
                }
                itemMeta.setLore(lore);
                itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "customSkin:"+s);
                itemStack.setItemMeta(itemMeta);

                itemStackHashMap.put(s, itemStack);
                itemStacksStringList.add(s);
                loadAmount++;
            }

        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6SkinTicket&7> &a成功載入 &f" + loadAmount + "&a 項造型。"));
    }

    public void SkinRemover() {

        Material itemMaterial = Material.getMaterial(SkinTicket.getInstance().getConfig().getString("SkinRemover.displayMaterial").toUpperCase(Locale.ROOT));
        ItemStack itemStack = new ItemStack(itemMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("SkinRemover.displayName")));
        itemMeta.setCustomModelData(SkinTicket.getInstance().getConfig().getInt("SkinRemover.customModelData"));
        List<String> lore = new ArrayList<>();
        for (String l : SkinTicket.getInstance().getConfig().getStringList("SkinRemover.displayLore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', l));
        }
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "skinRemover");
        itemStack.setItemMeta(itemMeta);
        skinRemover = itemStack;
        return;
    }

}