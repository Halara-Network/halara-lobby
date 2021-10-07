package me.coopersully.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HalaraLobbyPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        if (Bukkit.getPluginManager().getPlugin("Halara-Core") == null) {
            System.out.println("Halara-Core not detected, disabling Halara-Lobby.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        switch (label) {
            case "fixspeeds", "resetspeeds" -> {
                LobbyCommands.fixPlayerSpeeds(sender);
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Location spawn = new Location(Bukkit.getWorld("world"), 0.5, 50.0, 0.5, 0, 0);
        player.teleport(spawn);

        player.setGameMode(GameMode.ADVENTURE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
        player.setWalkSpeed(0);

        me.coopersully.Core.CoreCommands.openServerGUI(event.getPlayer());

    }

}
