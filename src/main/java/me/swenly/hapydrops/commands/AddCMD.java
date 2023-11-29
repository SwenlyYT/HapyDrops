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

public class AddCMD {
    public AddCMD (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        String chest_name = args[1];
        Integer item_chance;

        try {
            item_chance = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException exception) {
            sender.sendMessage("§cОшибка! Шанс указан неправильно!");
            return;
        }

        ItemStack itemStack = player.getItemInHand();

        try {
            File plugin_path = HapyDrops.plugin_path;
            File file = new File(plugin_path + "/chest_data/" + chest_name + ".yml");

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);

            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("Data").createSection(String.valueOf(yamlConfiguration.getConfigurationSection("Data").getKeys(false).size() + 1));

            configurationSection.set("Chance", item_chance);
            Map<String, Object> itemMap = itemStack.serialize();

            if (!itemMap.containsKey("==")) {
                itemMap.put("==", "org.bukkit.inventory.ItemStack");
            }

            configurationSection.createSection("Item", itemMap);

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(yamlConfiguration.saveToString());
            bw.flush();
            bw.close();

            sender.sendMessage("§aВы добавили §6" + itemStack.getType().toString().toUpperCase() + "§aв сундук!");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
