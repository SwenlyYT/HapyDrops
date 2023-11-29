package me.swenly.hapydrops.gui;

import me.swenly.hapydrops.HapyDrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class MysteriousChestGUI implements Listener {
    private final Inventory inventory;

    public MysteriousChestGUI() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inventory = Bukkit.createInventory(null, 54, "Example");

        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        try {
            File plugin_path = HapyDrops.plugin_path;
            File file = new File(plugin_path + "/chest_data/mysterious.yml");

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);

            ConfigurationSection dataSection = yamlConfiguration.getConfigurationSection("Data");
            Map<String, Object> dataMap = dataSection.getValues(false);

            Integer last_index = 0;
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                ConfigurationSection item_data = (ConfigurationSection) entry.getValue();

                Integer chance = item_data.getInt("Chance");
                ItemStack itemStack = item_data.getItemStack("Item");

                Random random = new Random();
                Integer number = random.nextInt(99) + 1;

                if (number <= chance) {
                    inventory.setItem(last_index, itemStack);
                    last_index++;
                }
            }

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inventory);
    }
    public Inventory getInventory() { return inventory; }
}