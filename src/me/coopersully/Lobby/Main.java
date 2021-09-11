package me.coopersully.Lobby;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static me.coopersully.Lobby.Command.fixPlayerSpeeds;
import static me.coopersully.Lobby.Command.openServerGUI;

public class Main extends JavaPlugin implements Listener {

    public static Inventory server_selector;

    public static ItemStack survival_item = new ItemStack(Material.OAK_LOG);
    public static ItemMeta survival_meta = survival_item.getItemMeta();

    public static ItemStack creative_item = new ItemStack(Material.DIAMOND_BLOCK);
    public static ItemMeta creative_meta = creative_item.getItemMeta();

    public static ItemStack prototype_item = new ItemStack(Material.AMETHYST_BLOCK);
    public static ItemMeta prototype_meta = prototype_item.getItemMeta();

    // Triggers on server startup, restart, and reload
    @Override
    public void onEnable() {

        // Enable all listeners
        getServer().getPluginManager().registerEvents(this, this);

        // Create server selector GUI
        server_selector = Bukkit.createInventory(null, 9, ChatColor.WHITE + "\uF808\uF808\uF808\uF808\uF808\uF808\uF808\uF808\uF808\uF808\uF069\uF801\uF070");

        // Create data for public ItemStacks
        createItemStackData();

        // Bungeecord
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

    }

    // Triggers on shutdown and reloads
    @Override
    public void onDisable() {

    }

    // Create data for public ItemStacks
    public void createItemStackData() {

        // Create ItemStack data for the Survival server icon

        survival_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lEnhanced Survival &a[1.17+]"));

        List<String> survival_lore = new ArrayList<>();
        survival_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "An enhanced version of the survival");
        survival_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "experience; collect, combat, and build.");
        survival_lore.add("");
        survival_lore.add(ChatColor.DARK_GRAY + "» Click to select this server");
        survival_meta.setLore(survival_lore);

        survival_item.setItemMeta(survival_meta);

        // Create ItemStack data for the Creative server icon

        creative_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lCreative &a[1.17+]"));

        List<String> creative_lore = new ArrayList<>();
        creative_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "A freestyle, sandbox gamemode in which");
        creative_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "players build to their heart's content.");
        creative_lore.add("");
        creative_lore.add(ChatColor.DARK_GRAY + "» Click to select this server");
        creative_meta.setLore(creative_lore);

        creative_item.setItemMeta(creative_meta);

        // Create ItemStack data for the Prototype server icon

        prototype_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lPrototype &a[1.17+]"));

        List<String> prototype_lore = new ArrayList<>();
        prototype_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This gamemode is currently undergoing");
        prototype_lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "maintenance; please check back later.");
        prototype_lore.add("");
        prototype_lore.add(ChatColor.DARK_GRAY + "» Click to select this server");
        prototype_meta.setLore(prototype_lore);

        prototype_item.setItemMeta(prototype_meta);

    }

    // All existing commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        switch (label) {
            case "server-selector", "servers", "play" -> {
                openServerGUI(sender, cmd, label, args);
                return true;
            }
            case "fixspeeds" -> {
                fixPlayerSpeeds(sender, cmd, label, args);
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

        PlayerInventory inv = player.getInventory();

        // Clear inventory
        inv.clear();

        // Give server icon items
        inv.setItem(11, survival_item);
        inv.setItem(13, creative_item);
        inv.setItem(15, prototype_item);

        // Open inventory
        player.openInventory(server_selector);

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) {
            return;
        }
        if (!((event.getCurrentItem().isSimilar(survival_item)) || (event.getCurrentItem().isSimilar(creative_item)) || (event.getCurrentItem().isSimilar(prototype_item)))) {
            return;
        }

        event.setCancelled(true); // Disallow taking items from GUI

        Player player = (Player) event.getWhoClicked();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");

        if (event.getSlot() == 11) {
            // Selected survival server
            out.writeUTF("survival");
            player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eHalara &r» &7Transferring you to the &aSurvival&7 server..."));
        }

        if (event.getSlot() == 13) {
            // Selected creative server
            out.writeUTF("creative");
            player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eHalara &r» &7Transferring you to the &aCreative&7 server..."));
        }

        if (event.getSlot() == 15) {
            // Selected prototype server
            out.writeUTF("prototype");
            player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eHalara &r» &7Transferring you to the &aPrototype&7 lobby..."));
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if (!(event.getInventory() == server_selector)) {
            return;
        }

        PlayerInventory inv = player.getInventory();
        inv.removeItem(survival_item, creative_item, prototype_item);

    }

}
