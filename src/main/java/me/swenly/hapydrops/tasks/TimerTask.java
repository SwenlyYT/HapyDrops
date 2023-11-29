package me.swenly.hapydrops.tasks;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.swenly.hapydrops.HapyDrops;
import me.swenly.hapydrops.config.ConfigSystem;
import me.swenly.hapydrops.config.PlaceHolders;
import me.swenly.hapydrops.gui.MysteriousChestGUI;
import me.swenly.hapydrops.gui.MysticChestGUI;
import me.swenly.hapydrops.utils.ChestUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimerTask extends BukkitRunnable {
    public final JavaPlugin plugin = HapyDrops.getPlugin(HapyDrops.class);
    public final HolographicDisplaysAPI HoloAPI = HolographicDisplaysAPI.get(plugin);
    public static Block mysticChest = null;
    public static Block mysteriousChest = null;
    public static Boolean mysticClosed = true;
    public static Boolean mysteriousClosed = true;
    public static Hologram mysticHologram = null;
    public static Hologram mysteriousHologram = null;

    public static MysticChestGUI mysticChestGUI = null;

    public static MysteriousChestGUI mysteriousChestGUI = null;

    public static Integer last_spawn_hour = 25;
    public static Integer last_warn_hour = 25;

    public static Integer last_15m_hour = 25;
    public static Integer last_10m_hour = 25;
    public static Integer last_5m_hour = 25;
    public static Integer last_3m_hour = 25;
    public static Integer last_1m_hour = 25;
    public static Integer last_30s_hour = 25;
    public static Integer last_15s_hour = 25;
    public static Integer last_5s_hour = 25;
    public static Integer last_4s_hour = 25;
    public static Integer last_3s_hour = 25;
    public static Integer last_2s_hour = 25;
    public static Integer last_1s_hour = 25;

    public TimerTask() {
    }

    @Override
    public void run() {
        /*
        LocalDateTime oldTime = HapyDrops.firstTime;
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime newTime = Timestamp.from(Instant.ofEpochMilli(Timestamp.valueOf(nowTime).getTime() - Timestamp.valueOf(oldTime).getTime())).toLocalDateTime();

        Integer hour = newTime.getHour();
        Integer minute = newTime.getMinute();
        Integer second = newTime.getSecond();

        if (minute % 2 == 1) {
            if (second == 55) {
                plugin.getServer().broadcastMessage("через 5 сек таинственный");
                System.out.println(minute + " " + second);
            }
            else if (second == 56) {
                plugin.getServer().broadcastMessage("через 4 сек таинственный");
            }
            else if (second == 57) {
                plugin.getServer().broadcastMessage("через 3 сек таинственный");
            }
            else if (second == 58) {
                plugin.getServer().broadcastMessage("через 2 сек таинственный");
            }
            else if (second == 59) {
                plugin.getServer().broadcastMessage("через 1 сек таинственный");
            }
        }

        else if (minute != 0) {
            if (second == 55) {
                plugin.getServer().broadcastMessage("через 5 сек мистический");
            }
            else if (second == 56) {
                plugin.getServer().broadcastMessage("через 4 сек мистический");
            }
            else if (second == 57) {
                plugin.getServer().broadcastMessage("через 3 сек мистический");
            }
            else if (second == 58) {
                plugin.getServer().broadcastMessage("через 2 сек мистический");
            }
            else if (second == 59) {
                plugin.getServer().broadcastMessage("через 1 сек мистический");
            }
        }
        */

        // 0:00, МИСТИЧЕСКИЙ ГРУЗ
        // 3:00, МИСТИЧЕСКИЙ ГРУЗ
        // 8:00, ТАИНСТВЕННЫЙ ГРУЗ
        // 12:00, МИСТИЧЕСКИЙ ГРУЗ
        // 16:00, ТАИНСТВЕННЫЙ ГРУЗ
        // 20:00, МИСТИЧЕСКИЙ ГРУЗ
        // 23:00, МИСТИЧЕСКИЙ ГРУЗ

        World world = Bukkit.getWorld("world");

        FileConfiguration config = ConfigSystem.getConfig();
        LocalDateTime nowTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        Integer hour = nowTime.getHour();
        Integer minute = nowTime.getMinute();
        Integer second = nowTime.getSecond();

        if ((mysticClosed && mysticChest != null) || (mysteriousClosed && mysteriousChest != null)) {
            Integer minLeft = 60 - minute - 1;
            Integer secLeft = 60 - second - 1;

            Integer minLeftLastChar = Integer.parseInt((minLeft + "").substring((minLeft + "").length() - 1));
            Integer secLeftLastChar = Integer.parseInt((secLeft + "").substring((secLeft + "").length() - 1));

            String minLeftWord = "";
            String secLeftWord = "";

            if (minLeftLastChar >= 5 || minLeftLastChar == 0 || (minLeft > 10 && minLeft < 15)) {
                minLeftWord = "минут";
            } else if (minLeftLastChar >= 2) {
                minLeftWord = "минуты";
            }
            else {
                minLeftWord = "минута";
            }

            if (secLeftLastChar >= 5 || secLeftLastChar == 0 || (secLeft > 10 && secLeft < 15)) {
                secLeftWord = "секунд";
            } else if (secLeftLastChar >= 2) {
                secLeftWord = "секунды";
            }
            else {
                secLeftWord = "секунда";
            }

            PlaceHolders.addPlaceholder("%m", minLeft + " " + minLeftWord);
            PlaceHolders.addPlaceholder("%s", secLeft + " " + secLeftWord);

            if (mysticChest != null) {
                if (mysticHologram == null) {
                    Location holoLoc = mysticChest.getLocation();
                    holoLoc.setX(mysticChest.getLocation().getBlockX() + 0.5);
                    holoLoc.setZ(mysticChest.getLocation().getBlockZ() + 0.5);
                    holoLoc.setY(world.getHighestBlockYAt(holoLoc) + config.getStringList("mystic_chest_closed_hologram").size());

                    mysticHologram = HoloAPI.createHologram(holoLoc);

                    for (String holoString : config.getStringList("mystic_chest_closed_hologram")) {
                        mysticHologram.getLines().appendText(PlaceHolders.formatText(holoString));
                    }
                }
                else {
                    mysticHologram.getLines().clear();

                    for (String holoString : config.getStringList("mystic_chest_closed_hologram")) {
                        mysticHologram.getLines().appendText(PlaceHolders.formatText(holoString));
                    }
                }
            }

            if (mysteriousChest != null) {
                if (mysteriousHologram == null) {
                    Location holoLoc = mysteriousChest.getLocation();
                    holoLoc.setX(mysteriousChest.getLocation().getBlockX() + 0.5);
                    holoLoc.setZ(mysteriousChest.getLocation().getBlockZ() + 0.5);
                    holoLoc.setY(world.getHighestBlockYAt(holoLoc) + config.getStringList("mysterious_chest_closed_hologram").size());

                    mysteriousHologram = HoloAPI.createHologram(holoLoc);

                    for (String holoString : config.getStringList("mysterious_chest_closed_hologram")) {
                        mysteriousHologram.getLines().appendText(PlaceHolders.formatText(holoString));
                    }
                }
                else {
                    mysteriousHologram.getLines().clear();

                    for (String holoString : config.getStringList("mysterious_chest_closed_hologram")) {
                        mysteriousHologram.getLines().appendText(PlaceHolders.formatText(holoString));
                    }
                }
            }
        }

        if (hour == 2 || hour == 11 || hour == 19 || hour == 22) {
            if (second == 0 || second == 1) {
                try {
                    PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysticChest.getZ());
                } catch (Exception ignored) {}

                switch (minute) {
                    case 60-15:
                        if (last_warn_hour.equals(hour)) {
                            return;
                        }

                        if (mysticChest != null) {
                            mysticChest.setType(Material.AIR);
                        }

                        mysticChest = ChestUtils.moveChestToRandomLoc(Material.ENDER_CHEST);
                        mysticChestGUI = new MysticChestGUI();
                        mysticClosed = true;

                        PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                        PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                        PlaceHolders.addPlaceholder("%z", mysticChest.getZ());

                        PlaceHolders.addPlaceholder("{x}", mysticChest.getX());
                        PlaceHolders.addPlaceholder("{y}", mysticChest.getY());
                        PlaceHolders.addPlaceholder("{z}", mysticChest.getZ());

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_15m_message")));
                        last_warn_hour = hour;
                        break;
                    case 60-10:
                        if (last_10m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_10m_message")));
                        last_10m_hour = hour;
                        break;
                    case 60-5:
                        if (last_5m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_5m_message")));
                        last_5m_hour = hour;
                        break;
                    case 60-3:
                        if (last_3m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_3m_message")));
                        last_3m_hour = hour;
                        break;
                    case 60-1:
                        if (last_1m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_1m_message")));
                        last_1m_hour = hour;
                        break;
                }
            }
            if (minute == 59) {
                try {
                    PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysticChest.getZ());
                } catch (Exception ignored) {}

                switch (second) {
                    case 60-30:
                        if (last_30s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_30s_message")));
                        last_30s_hour = hour;
                        break;
                    case 60-15:
                        if (last_15s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_15s_message")));
                        last_15s_hour = hour;
                        break;
                    case 60-5:
                        if (last_5s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_5s_message")));
                        last_5s_hour = hour;
                        break;
                    case 60-4:
                        if (last_4s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_4s_message")));
                        last_4s_hour = hour;
                        break;
                    case 60-3:
                        if (last_3s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_3s_message")));
                        last_3s_hour = hour;
                        break;
                    case 60-2:
                        if (last_2s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_2s_message")));
                        last_2s_hour = hour;
                        break;
                    case 60-1:
                        if (last_1s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_1s_message")));
                        last_1s_hour = hour;
                        break;
                }
            }
        }
        else if (hour == 0 || hour == 3 || hour == 12 || hour == 20) {
            if (minute == 0 && (second == 0 || second == 1)) {
                if (last_spawn_hour.equals(hour)) {
                    return;
                }

                if (mysticHologram != null) {
                    mysticHologram.delete();
                }

                ChestUtils.spawnMysticChest();

                plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_message")));

                mysticClosed = false;
                last_spawn_hour = hour;
            }
        }
        else if (hour == 7 || hour == 15) {
            if (second == 0 || second == 1) {
                try {
                    PlaceHolders.addPlaceholder("%x", mysteriousChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysteriousChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysteriousChest.getZ());
                } catch (Exception ignored) {}

                switch (minute) {
                    case 60-30:
                        if (last_warn_hour.equals(hour)) {
                            return;
                        }

                        if (mysteriousChest != null) {
                            mysteriousChest.setType(Material.AIR);
                        }

                        mysteriousChest = ChestUtils.moveChestToRandomLoc(Material.RESPAWN_ANCHOR);
                        mysteriousChestGUI = new MysteriousChestGUI();
                        mysteriousClosed = true;

                        PlaceHolders.addPlaceholder("%x", mysteriousChest.getX());
                        PlaceHolders.addPlaceholder("%y", mysteriousChest.getY());
                        PlaceHolders.addPlaceholder("%z", mysteriousChest.getZ());

                        PlaceHolders.addPlaceholder("{x}", mysteriousChest.getX());
                        PlaceHolders.addPlaceholder("{y}", mysteriousChest.getY());
                        PlaceHolders.addPlaceholder("{z}", mysteriousChest.getZ());

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_30m_message")));
                        last_warn_hour = hour;
                        break;
                    case 60-15:
                        if (last_15m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_15m_message")));
                        last_15m_hour = hour;
                        break;
                    case 60-10:
                        if (last_10m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_10m_message")));
                        last_10m_hour = hour;
                        break;
                    case 60-5:
                        if (last_5m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_5m_message")));
                        last_5m_hour = hour;
                        break;
                    case 60-3:
                        if (last_3m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_3m_message")));
                        last_3m_hour = hour;
                        break;
                    case 60-1:
                        if (last_1m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_1m_message")));
                        last_1m_hour = hour;
                        break;
                }
            }
            if (minute == 59) {
                try {
                    PlaceHolders.addPlaceholder("%x", mysteriousChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysteriousChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysteriousChest.getZ());
                } catch (Exception ignored) {}

                switch (second) {
                    case 60-30:
                        if (last_30s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_30s_message")));
                        last_30s_hour = hour;
                        break;
                    case 60-15:
                        if (last_15s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_15s_message")));
                        last_15s_hour = hour;
                        break;
                    case 60-5:
                        if (last_5s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_5s_message")));
                        last_5s_hour = hour;
                        break;
                    case 60-4:
                        if (last_4s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_4s_message")));
                        last_4s_hour = hour;
                        break;
                    case 60-3:
                        if (last_3s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_3s_message")));
                        last_3s_hour = hour;
                        break;
                    case 60-2:
                        if (last_2s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_2s_message")));
                        last_2s_hour = hour;
                        break;
                    case 60-1:
                        if (last_1s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_1s_message")));
                        last_1s_hour = hour;
                        break;
                }
            }
        }
        else if (hour == 8 || hour == 16) {
            if (second == 0 || second == 1) {
                if (last_spawn_hour.equals(hour)) {
                    return;
                }

                ChestUtils.spawnMysteriousChest();

                plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_spawn_message")));

                mysteriousClosed = false;
                last_spawn_hour = hour;
            }
        }
        else if (hour == 23) {
            if (second == 0 || second == 1) {
                if (mysticChest == null) {
                    return;
                }

                Location holoLoc = mysticChest.getLocation();

                try {
                    PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysticChest.getZ());
                } catch (Exception ignored) {}

                switch (minute) {
                    case 60-15:
                        if (last_warn_hour.equals(hour)) {
                            return;
                        }

                        if (mysticChest != null) {
                            mysticChest.setType(Material.AIR);
                        }

                        mysticChest = ChestUtils.moveChestToRandomLoc(Material.ENDER_CHEST);
                        mysticChestGUI = new MysticChestGUI();
                        mysticClosed = true;

                        PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                        PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                        PlaceHolders.addPlaceholder("%z", mysticChest.getZ());

                        PlaceHolders.addPlaceholder("{x}", mysticChest.getX());
                        PlaceHolders.addPlaceholder("{y}", mysticChest.getY());
                        PlaceHolders.addPlaceholder("{z}", mysticChest.getZ());

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_15m_message")));
                        last_warn_hour = hour;
                        break;
                    case 60-10:
                        if (last_10m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_10m_message")));
                        last_10m_hour = hour;
                        break;
                    case 60-5:
                        if (last_5m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_5m_message")));
                        last_5m_hour = hour;
                        break;
                    case 60-3:
                        if (last_3m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_3m_message")));
                        last_3m_hour = hour;
                        break;
                    case 60-1:
                        if (last_1m_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_1m_message")));
                        last_1m_hour = hour;
                        break;
                    case 0:
                        if (last_spawn_hour.equals(hour)) {
                            return;
                        }

                        ChestUtils.spawnMysticChest();

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_message")));

                        mysticClosed = false;
                        last_spawn_hour = hour;
                }
            }
            if (minute == 59) {
                try {
                    PlaceHolders.addPlaceholder("%x", mysticChest.getX());
                    PlaceHolders.addPlaceholder("%y", mysticChest.getY());
                    PlaceHolders.addPlaceholder("%z", mysticChest.getZ());
                } catch (Exception ignored) {}

                switch (second) {
                    case 60-30:
                        if (last_30s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_30s_message")));
                        last_30s_hour = hour;
                        break;
                    case 60-15:
                        if (last_15s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_15s_message")));
                        last_15s_hour = hour;
                        break;
                    case 60-5:
                        if (last_5s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_5s_message")));
                        last_5s_hour = hour;
                        break;
                    case 60-4:
                        if (last_4s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_4s_message")));
                        last_4s_hour = hour;
                        break;
                    case 60-3:
                        if (last_3s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_3s_message")));
                        last_3s_hour = hour;
                        break;
                    case 60-2:
                        if (last_2s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_2s_message")));
                        last_2s_hour = hour;
                        break;
                    case 60-1:
                        if (last_1s_hour.equals(hour)) {
                            return;
                        }

                        plugin.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_spawn_1s_message")));
                        last_1s_hour = hour;
                        break;
                }
            }

//            try {
//                if (mysticClosed && mysticChest.getType().equals(Material.ENDER_CHEST)) {
//                    PlaceHolders.addPlaceholder("%m", 60-minute);
//                    PlaceHolders.addPlaceholder("%s", 60-second);
//
//                    if (mysticHologram != null) {
//                        mysticHologram.delete();
//                    }
//
//                    Location location = new Location(Bukkit.getWorld("world"), mysticChest.getLocation().getX(), mysticChest.getLocation().getY() + 3, mysticChest.getLocation().getZ());
//
//                    mysticHologram = HoloAPI.createHologram(location);
//                    mysticHologram.getLines().appendText("");
//                    mysticHologram.getLines().appendText(PlaceHolders.formatText(config.getString("mystic_chest_timer_hologram")));
//                    mysticHologram.getLines().appendText("");
//                }
//                if (mysteriousClosed && mysteriousChest.getType().equals(Material.RESPAWN_ANCHOR)) {
//                    PlaceHolders.addPlaceholder("%m", 60-minute);
//                    PlaceHolders.addPlaceholder("%s", 60-second);
//
//                    if (mysteriousHologram != null) {
//                        mysteriousHologram.delete();
//                    }
//
//                    Location location = new Location(Bukkit.getWorld("world"), mysteriousChest.getLocation().getX(), mysteriousChest.getLocation().getY() + 3, mysteriousChest.getLocation().getZ());
//
//                    mysteriousHologram = HoloAPI.createHologram(location);
//                    mysteriousHologram.getLines().appendText("");
//                    mysteriousHologram.getLines().appendText(PlaceHolders.formatText(config.getString("mysterious_chest_timer_hologram")));
//                    mysteriousHologram.getLines().appendText("");
//                }
//            }
//            catch (Exception ignored) {
//
//            }
        }
    }
}
