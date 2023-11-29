package me.swenly.hapydrops.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

public class HashMapUtils {
    public static ConfigurationSection deleteValueInSectionByMap(Map<String, Object> map, ConfigurationSection needValue) {
        Map<String, Object> newItems = new LinkedHashMap<>();
        Map<String, Object> finalItems = new LinkedHashMap<>();
        Object firstKey = map.keySet().toArray()[0];
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        ConfigurationSection items = yamlConfiguration.createSection("Data");
        String keyForNeedValue = "";
        Integer i = 0;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ConfigurationSection entrySection = (ConfigurationSection) entry.getValue();

            if (entrySection.get("Item").equals(needValue.get("Item")) && entrySection.get("Chance").equals(needValue.get("Chance"))) {
                System.out.println(entry.getKey() + " is needed!");
                keyForNeedValue = entry.getKey();
            }
            else {
                newItems.put(entry.getKey(), entrySection);
            }
        }

        for (i = 1; i <= newItems.size() + 1; i++) {
            ConfigurationSection section = (ConfigurationSection) newItems.get(i + "");

            if (Integer.parseInt(keyForNeedValue) > i) {
                items.set(i + "", section);
                System.out.println("Added " + i + " with value " + section);
            }
            else if (Integer.parseInt(keyForNeedValue) == i) System.out.println(i + " skipped!");
            else {
                items.set(i - 1 + "", section);
                System.out.println("Added " + (i - 1) + " with value " + section);
            }
        }

        System.out.println(finalItems.get("1"));
        return items;
    }
}
