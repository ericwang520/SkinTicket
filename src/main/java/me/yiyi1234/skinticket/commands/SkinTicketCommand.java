package me.yiyi1234.skinticket.commands;

import me.yiyi1234.skinticket.SkinTicket;
import me.yiyi1234.skinticket.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkinTicketCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("skinticket.op")) {
            // skinticket give <ID>
            if (args.length < 1) {
                return false;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {

                    SkinTicket.getInstance().reloadConfig();
                    ItemManager itemManager = new ItemManager();
                    itemManager.itemList();
                    itemManager.SkinRemover();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.reload")));
                    return false;
                }
            }else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("SkinRemover")) {
                        Player player = (Player) sender;
                        player.getInventory().setItem(player.getInventory().firstEmpty(), SkinTicket.getInstance().itemManager.skinRemover);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.successGiveItem").replace("%item%", args[1])));
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.consoleLog").replace("%item%", args[1]).replace("%id%", sender.getName())));
                        return false;
                    }
                    if (SkinTicket.getInstance().itemManager.itemStackHashMap.containsKey(args[1])) {
                        Player player = (Player) sender;
                        player.getInventory().setItem(player.getInventory().firstEmpty(), SkinTicket.getInstance().itemManager.itemStackHashMap.get(args[1]));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.successGiveItem").replace("%item%", args[1])));
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.consoleLog").replace("%item%", args[1]).replace("%id%", sender.getName())));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.invalidSkin").replace("%item%", args[1])));
                    }

                    return false;
                }

            }else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.commandUsage")));
            }


        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkinTicket.getInstance().getConfig().getString("translate.noPermission")));

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("skinticket.op")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("give");
                return arguments;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("SkinRemover");
                    for (String s : SkinTicket.getInstance().itemManager.itemStacksStringList) {
                        arguments.add(s);
                    }
                    return arguments;
                }
            }
        }
        return null;
    }
}
