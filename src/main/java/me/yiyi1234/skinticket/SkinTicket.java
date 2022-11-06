package me.yiyi1234.skinticket;

import me.yiyi1234.skinticket.commands.SkinTicketCommand;
import me.yiyi1234.skinticket.events.InventoryClick;
import me.yiyi1234.skinticket.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SkinTicket extends JavaPlugin {
    private static SkinTicket instance;
    private FileConfiguration config;
    public ItemManager itemManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        config();
        getCommand("SkinTicket").setExecutor(new SkinTicketCommand());
        getCommand("SkinTicket").setTabCompleter(new SkinTicketCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        ItemManager itemManager = new ItemManager();
        itemManager.itemList();
        itemManager.SkinRemover();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6SkinTicket&7> &aSkinTicket Version 1.0 啟動成功"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6SkinTicket&7> &6製作者 依依#1350"));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void config() {
        File file = new File(this.getDataFolder().getAbsolutePath() + "/config.yml");
        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage("§7<§6SkinTicket§7> §f正在生成config！");
            this.getDataFolder().mkdir();
            this.saveResource("config.yml", false);

        }

        config = YamlConfiguration.loadConfiguration(file);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void reloadConfig() {
        super.reloadConfig();

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public static void setInstance(SkinTicket instance) {
        SkinTicket.instance = instance;
    }

    public static SkinTicket getInstance() {
        return instance;
    }
}
