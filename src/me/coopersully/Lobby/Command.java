package me.coopersully.Lobby;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class Command {

    public static void openServerGUI(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        // If the command's sender is a player
        if (sender instanceof Player) {

            Player player = (Player) sender;
            PlayerInventory inv = player.getInventory();

            // Clear inventory
            inv.clear();

            // Give server icon items
            inv.setItem(11, Main.survival_item);
            inv.setItem(13, Main.creative_item);
            inv.setItem(15, Main.prototype_item);

            // Open the "Server Selector" GUI
            player.openInventory(Main.server_selector);

        }
        // If the command's sender is !player
        else {

            sender.sendMessage(ChatColor.RED + "This command cannot be performed by the console.");

        }
    }

    public static void fixPlayerSpeeds(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        // If the command's sender is a player
        if (sender instanceof Player) {

            Player player = (Player) sender;

            player.setFlySpeed(0.1F);
            player.setWalkSpeed(0.2F);

            sender.sendMessage(ChatColor.GREEN + "All player speeds reset to their default values.");

        }
        // If the command's sender is !player
        else {

            sender.sendMessage(ChatColor.RED + "This command cannot be performed by the console.");

        }

    }

}
