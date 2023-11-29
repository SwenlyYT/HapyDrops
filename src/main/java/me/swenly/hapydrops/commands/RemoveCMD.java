package me.swenly.hapydrops.commands;

import me.swenly.hapydrops.HapyDrops;
import me.swenly.hapydrops.utils.HashMapUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RemoveCMD {
    public RemoveCMD (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String chest_name = args[1];
        Integer position = Integer.parseInt(args[2]);
        
        try {
            File plugin_path = HapyDrops.plugin_path;
            File file = new File(plugin_path + "/chest_data/" + chest_name + ".yml");

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);

            ConfigurationSection dataSection = yamlConfiguration.getConfigurationSection("Data");
            HashMap<String, Object> itemsMap = (LinkedHashMap<String, Object>) dataSection.getValues(false);

            ConfigurationSection itemData = dataSection.getConfigurationSection(position + "");

            dataSection = HashMapUtils.deleteValueInSectionByMap(itemsMap, itemData);
            yamlConfiguration.set("Data", dataSection);

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(yamlConfiguration.saveToString());
            bw.flush();
            bw.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
