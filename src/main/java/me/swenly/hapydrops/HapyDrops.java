package me.swenly.hapydrops;

import me.swenly.hapydrops.commands.CommandExecute;
import me.swenly.hapydrops.commands.CommandTabComplete;
import me.swenly.hapydrops.config.ConfigSystem;
import me.swenly.hapydrops.config.PlaceHolders;
import me.swenly.hapydrops.events.onBlockEvent;
import me.swenly.hapydrops.tasks.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public final class HapyDrops extends JavaPlugin {
    public static File plugin_path;
    public static BukkitTask timerTask;
    public static ArrayList<String> chests = new ArrayList<String>(Arrays.asList("mystic", "mysterious"));
    FileConfiguration config;

    @Override
    public void onEnable() {
        // Config
        this.saveDefaultConfig();
        config = this.getConfig();

        ConfigSystem.loadConfig(config);
        PlaceHolders.loadConfig(config);
        PlaceHolders.addPlaceholder("&", "§");

        // Commands
        getCommand("drops").setExecutor(new CommandExecute());
        getCommand("drops").setTabCompleter(new CommandTabComplete());

        // Events
        Bukkit.getPluginManager().registerEvents(new onBlockEvent(), this);

        // Tasks
        timerTask = new TimerTask().runTaskTimer(this, 0L, 20L); // 20L = 1 sec;

        // Yaml
        plugin_path = HapyDrops.getPlugin(HapyDrops.class).getDataFolder();;

        for (String chestName : chests) {
            File chest_file = new File(plugin_path + "/chest_data/" + chestName + ".yml");

            if (!chest_file.exists()) {
                chest_file.getParentFile().mkdir();

                try {
                    chest_file.createNewFile();

                    YamlConfiguration yamlConfiguration = new YamlConfiguration();
                    yamlConfiguration.createSection("Data");

                    FileWriter fileWriter = new FileWriter(chest_file);
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(yamlConfiguration.saveToString());
                    bw.flush();
                    bw.close();
                } catch (Exception exception) {
                    System.out.println("Can't create " + chestName + ".json");
                }
            }
        }
    }

    @Override
    public void onDisable() {
        timerTask.cancel();
    }
}
