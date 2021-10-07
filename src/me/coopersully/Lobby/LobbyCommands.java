package me.coopersully.Lobby;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommands {

    public static void fixPlayerSpeeds(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be performed by the console.");
            return;
        }

            Player player = (Player) sender;

            player.setFlySpeed(0.1F);
            player.setWalkSpeed(0.2F);

            sender.sendMessage(ChatColor.GREEN + "All player speeds reset to their default values.");

    }

}
