package me.swenly.hapydrops.utils;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.swenly.hapydrops.HapyDrops;
import me.swenly.hapydrops.config.ConfigSystem;
import me.swenly.hapydrops.config.PlaceHolders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

import static me.swenly.hapydrops.tasks.TimerTask.*;

public class ChestUtils {
    public static final JavaPlugin plugin = HapyDrops.getPlugin(HapyDrops.class);

    public static void spawnTNT(World world, Location holoLoc) {
        TNTPrimed tnt = world.spawn(holoLoc, TNTPrimed.class);

        tnt.setFuseTicks(0);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (((player.getLocation().getX() - holoLoc.getX()) <= 10 && (player.getLocation().getX() - holoLoc.getX()) >= -10) && ((player.getLocation().getY() - holoLoc.getY()) <= 10 && (player.getLocation().getY() - holoLoc.getY()) >= -10) && ((player.getLocation().getZ() - holoLoc.getZ()) <= 10 && (player.getLocation().getZ() - holoLoc.getZ()) >= -10)) {
                boolean haveArmor = false;

                for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                    if (itemStack != null && !(itemStack.getType().equals(Material.AIR))) {
                        haveArmor = true;
                        break;
                    }
                }

                if (!haveArmor) {
                    player.setHealth(0);
                }
            }
        }
    }

    public static void spawnMysticChest() {
        FileConfiguration config = ConfigSystem.getConfig();
        HolographicDisplaysAPI HoloAPI = HolographicDisplaysAPI.get(plugin);

        if (mysticHologram != null) {
            mysticHologram.delete();
        }

        World world = Bukkit.getWorld("world");

        if (mysticChest == null) {
            return;
        }

        Location holoLoc = mysticChest.getLocation();
        holoLoc.setX(mysticChest.getLocation().getBlockX() + 0.5);
        holoLoc.setZ(mysticChest.getLocation().getBlockZ() + 0.5);
        holoLoc.setY(world.getHighestBlockYAt(holoLoc) + config.getStringList("mystic_chest_opened_hologram").size());

        mysticHologram = HoloAPI.createHologram(holoLoc);

        for (String holoString : config.getStringList("mystic_chest_opened_hologram")) {
            mysticHologram.getLines().appendText(PlaceHolders.formatText(holoString));
        }

        spawnTNT(world, holoLoc);

        PlaceHolders.addPlaceholder("%x", mysticChest.getX());
        PlaceHolders.addPlaceholder("%y", mysticChest.getY());
        PlaceHolders.addPlaceholder("%z", mysticChest.getZ());

        PlaceHolders.addPlaceholder("{x}", mysticChest.getX());
        PlaceHolders.addPlaceholder("{y}", mysticChest.getY());
        PlaceHolders.addPlaceholder("{z}", mysticChest.getZ());
    }

    public static void spawnMysteriousChest() {
        FileConfiguration config = ConfigSystem.getConfig();
        HolographicDisplaysAPI HoloAPI = HolographicDisplaysAPI.get(plugin);

        if (mysteriousHologram != null) {
            mysteriousHologram.delete();
        }

        World world = Bukkit.getWorld("world");

        if (mysteriousChest == null) {
            return;
        }

        Location holoLoc = mysteriousChest.getLocation();
        holoLoc.setX(mysteriousChest.getLocation().getBlockX() + 0.5);
        holoLoc.setZ(mysteriousChest.getLocation().getBlockZ() + 0.5);
        holoLoc.setY(world.getHighestBlockYAt(holoLoc) + config.getStringList("mysterious_chest_opened_hologram").size());

        mysteriousHologram = HoloAPI.createHologram(holoLoc);

        for (String holoString : config.getStringList("mysterious_chest_opened_hologram")) {
            mysteriousHologram.getLines().appendText(PlaceHolders.formatText(holoString));
        }

        spawnTNT(world, holoLoc);

        PlaceHolders.addPlaceholder("%x", mysteriousChest.getX());
        PlaceHolders.addPlaceholder("%y", mysteriousChest.getY());
        PlaceHolders.addPlaceholder("%z", mysteriousChest.getZ());

        PlaceHolders.addPlaceholder("{x}", mysteriousChest.getX());
        PlaceHolders.addPlaceholder("{y}", mysteriousChest.getY());
        PlaceHolders.addPlaceholder("{z}", mysteriousChest.getZ());
    }

    public static Block moveChestToRandomLoc(Material material) {
        FileConfiguration config = ConfigSystem.getConfig();

        Random random = new Random();
        World world = Bukkit.getWorld("world");

        Location chestLoc;

        String chest_type;

        if (material.equals(Material.ENDER_CHEST)) chest_type = "mystic";
        else chest_type = "mysterious";

        int maxCoordinate = config.getInt(chest_type + "_chest_coordinate");

        while (true) {
            Integer x = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            Integer z = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            Integer y = world.getHighestBlockYAt(x, z);

            chestLoc = new Location(world, x, y + 1, z);

            if (new Location(world, x, y, z).getBlock().getType().equals(Material.WATER) && chestLoc.getBlock().getType().equals(Material.AIR)) {
                continue;
            }

            break;
        }

        Block chest = Bukkit.getWorld("world").getBlockAt(chestLoc);
        chest.setType(material, false);

        return chest;
    }
}
