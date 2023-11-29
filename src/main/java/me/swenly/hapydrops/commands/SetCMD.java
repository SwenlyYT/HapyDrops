package me.swenly.hapydrops.commands;

import me.swenly.hapydrops.HapyDrops;
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
import java.util.Map;

public class SetCMD {
    public SetCMD (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        String chest_name = args[1];
        String item_chance = args[2];
        Integer position = Integer.parseInt(args[3]);

        ItemStack itemStack = player.getItemInHand();

        try {
            File plugin_path = HapyDrops.plugin_path;
            File file = new File(plugin_path + "/chest_data/" + chest_name + ".yml");

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);

            ConfigurationSection dataSection = yamlConfiguration.getConfigurationSection("Data");

            ConfigurationSection itemData = dataSection.getConfigurationSection(position + "");

            itemData.set("Chance", item_chance);
            Map<String, Object> itemMap = itemStack.serialize();

            if (!itemMap.containsKey("==")) {
                itemMap.put("==", "org.bukkit.inventory.ItemStack");
            }

            itemData.createSection("Item", itemMap);

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
