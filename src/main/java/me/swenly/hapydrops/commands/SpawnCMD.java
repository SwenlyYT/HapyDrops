package me.swenly.hapydrops.commands;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.swenly.hapydrops.HapyDrops;
import me.swenly.hapydrops.config.ConfigSystem;
import me.swenly.hapydrops.config.PlaceHolders;
import me.swenly.hapydrops.gui.MysteriousChestGUI;
import me.swenly.hapydrops.gui.MysticChestGUI;
import me.swenly.hapydrops.utils.ChestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static me.swenly.hapydrops.tasks.TimerTask.*;

public class SpawnCMD {
    public final JavaPlugin plugin = HapyDrops.getPlugin(HapyDrops.class);
    public final HolographicDisplaysAPI HoloAPI = HolographicDisplaysAPI.get(plugin);

    public SpawnCMD (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config = ConfigSystem.getConfig();

        String chest_type = args[1];

        if (chest_type.toLowerCase().equals("mysterious")) {
            mysteriousChest = ChestUtils.moveChestToRandomLoc(Material.RESPAWN_ANCHOR);
            mysteriousChestGUI = new MysteriousChestGUI();
            mysteriousClosed = false;

            if (mysteriousHologram != null) {
                mysteriousHologram.delete();
                mysteriousHologram = null;
            }

            if (args.length >= 3) {
                Bukkit.getServer().broadcastMessage(mysteriousChest.getX() + " " + mysteriousChest.getY() + " " + mysteriousChest.getZ());

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    ChestUtils.spawnMysteriousChest();
                }, Integer.parseInt(args[2]) * 20L);
            }
            else {
                ChestUtils.spawnMysteriousChest();
            }
        } else if (chest_type.toLowerCase().equals("mystic")) {
            mysticChest = ChestUtils.moveChestToRandomLoc(Material.ENDER_CHEST);
            mysticChestGUI = new MysticChestGUI();
            mysticClosed = false;

            if (mysticHologram != null) {
                mysticHologram.delete();
                mysticHologram = null;
            }

            if (args.length >= 3) {
                Bukkit.getServer().broadcastMessage(mysticChest.getX() + " " + mysticChest.getY() + " " + mysticChest.getZ());

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    ChestUtils.spawnMysticChest();
                }, Integer.parseInt(args[2]) * 20L);
            }
            else {
                ChestUtils.spawnMysticChest();
            }
        }
        else {
            sender.sendMessage("§cОшибка! Такого сундука нет!");
        }
    }
}
