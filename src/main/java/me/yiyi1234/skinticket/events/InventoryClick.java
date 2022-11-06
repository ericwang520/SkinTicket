package me.yiyi1234.skinticket.events;

import me.yiyi1234.skinticket.SkinTicket;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class InventoryClick implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent event) {


        if (!event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            return;
        }

        // 判別 cursor 是否為 skin
        NamespacedKey namespacedKey = new NamespacedKey(SkinTicket.getInstance(), "skin");
        if (event.getCursor().getItemMeta() == null || event.getCursor().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING) == null) {
            return;
        }

        String skinNbt = event.getCursor().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        String[] skinNbtSplit = skinNbt.split(":");
        if (skinNbtSplit[0].contains("skinRemover")) {
            if (event.getCurrentItem().getAmount() != 1 || event.getCursor().getAmount() != 1) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING) != null) {
                String currentItemNbt = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
                String[] currentItemNbtSplit = currentItemNbt.split(":");
                if (!currentItemNbtSplit[0].contains("useCustomSkin")) {
                    return;
                }

                if (event.getClickedInventory().firstEmpty() == -1) {
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.bagPackFull")));
                    return;
                }

                ItemStack currentItem = event.getCurrentItem();
                ItemMeta currentItemMeta = currentItem.getItemMeta();

                currentItemMeta.getPersistentDataContainer().remove(namespacedKey);

                String displaySkinNameLore = SkinTicket.getInstance().getConfig().getString("translate.displaySkinNameLore").replace("%item%", currentItemNbtSplit[1]);
                List<String> lore = new ArrayList<>();
                for (String s : currentItemMeta.getLore()) {

                    if (!s.contains(ChatColor.translateAlternateColorCodes('&', displaySkinNameLore))) {
                        lore.add(s);
                    }
                }
                currentItemMeta.setLore(lore);


                if (currentItemNbtSplit[2].contains("null")) {
                    currentItemMeta.setCustomModelData(0);
                } else {
                    currentItemMeta.setCustomModelData(Integer.valueOf(currentItemNbtSplit[2]));
                }

                currentItem.setItemMeta(currentItemMeta);

                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));

                SkinTicket.getInstance().getServer().getScheduler().runTaskLater(SkinTicket.getInstance(), () -> {
                    event.getClickedInventory().setItem(event.getSlot(), currentItem);
                }, 1);
                event.getClickedInventory().setItem(event.getClickedInventory().firstEmpty(), SkinTicket.getInstance().itemManager.itemStackHashMap.get(currentItemNbtSplit[1]));
                player.updateInventory();

                return;

            }
        }

        if (skinNbtSplit[0].contains("customSkin")) {

            // 判別 currentItem 是否不是 skin 、 是否有套上 skin 、 是否為同一種材質

            if (event.getCurrentItem().getType() != event.getCursor().getType()) {
                return;
            }
            if (event.getCurrentItem().getAmount() != 1 || event.getCursor().getAmount() != 1) {
                return;
            }


            if (event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING) != null) {
                String currentItemNbt = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
                String[] currentItemNbtSplit = currentItemNbt.split(":");

                if (currentItemNbtSplit[0].contains("customSkin")) {
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.itemIsSkin")));
                    return;
                }
                if (currentItemNbtSplit[0].contains("skinRemover")) {
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.itemIsRemover")));
                    return;
                }
                if (currentItemNbtSplit[0].contains("useCustomSkin")) {
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.alreadyHaveSkin")));
                    return;
                }

            }


            ItemStack currentItem = event.getCurrentItem();
            ItemMeta currentItemMeta = currentItem.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.displaySkinNameLore").replace("%item%", skinNbtSplit[1])));

            if (currentItemMeta.getLore() != null) {
                for (String s : currentItemMeta.getLore()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s));
                }
            }
            currentItemMeta.setLore(lore);

            if (currentItemMeta.hasCustomModelData()) {
                currentItemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "useCustomSkin:" + skinNbtSplit[1] + ":" + currentItemMeta.getCustomModelData());
            } else {
                currentItemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "useCustomSkin:" + skinNbtSplit[1] + ":null");
            }
            currentItemMeta.setCustomModelData(event.getCursor().getItemMeta().getCustomModelData());
            currentItem.setItemMeta(currentItemMeta);


            Player player = (Player) event.getWhoClicked();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));

            player.updateInventory();

            SkinTicket.getInstance().getServer().getScheduler().runTaskLater(SkinTicket.getInstance(), () -> {

                event.getClickedInventory().setItem(event.getSlot(), currentItem);
            }, 1);

        }

    }

    // 備註
    // currentItem 及 cursorItem 都是在交換物品前的東西
    // current 及 cursor 沒東西時都是 AIR

    // NBT
    // skinNBT => customSkin:skinName
    // RemoverNBT => skinRemover
    // 崁上道具的NBT => useCustomSkin:skinName:原本的 CustomModelData

    // 崁入皮膚
    // 判別 cursor 是否為 skin 、 判別 currentItem 是否有裝上 skin 了 (如果 有 取消)、兩個是否為同一種 材質
    // 刪除 cursor 上的東西 和 currentItem 上的東西，並把 給予一個新的 currentItem (改過 custommodeldata 及 lore 第一行的 及 nbt )
    // 發送完成訊息

    // 拔除皮膚
    // 判別 cursor 是否為 Remover、判別 currentItem 是否有 skin (如果 沒有 取消) 、判斷背包是否滿了
    // 刪除 Remover 和 currentItem、給予 原本的 currentItem (刪除 nbt + lore + custommodeldata) 、 及造型。


}


