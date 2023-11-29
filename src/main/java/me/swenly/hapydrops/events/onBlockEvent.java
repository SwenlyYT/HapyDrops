package me.swenly.hapydrops.events;

import me.swenly.hapydrops.config.ConfigSystem;
import me.swenly.hapydrops.config.PlaceHolders;
import me.swenly.hapydrops.tasks.TimerTask;
import me.swenly.hapydrops.gui.MysteriousChestGUI;
import me.swenly.hapydrops.gui.MysticChestGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.swenly.hapydrops.tasks.TimerTask.*;

public class onBlockEvent implements Listener {
    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if ((mysticChest != null && mysticChest.equals(block)) || (mysteriousChest != null && mysteriousChest.equals(block))) {
                event.setCancelled(true);

                if (mysticChest != null && mysticChest.equals(block) && !mysticClosed) {
                    mysticChestGUI.openInventory(player);
                } else if (mysteriousChest != null && mysteriousChest.equals(block) && !mysteriousClosed) {
                    mysteriousChestGUI.openInventory(player);
                }
            }
        }
    }

    @EventHandler
    public void onChestInvClick(InventoryClickEvent event) {
        event.getWhoClicked();
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = event.getView();

        Inventory playerInventory = player.getInventory();
        Inventory topInventory = inventoryView.getTopInventory();

        if (mysticChestGUI == null && mysteriousChestGUI == null) {
            return;
        }

        if ((mysticChestGUI == null || !topInventory.equals(mysticChestGUI.getInventory())) && (mysteriousChestGUI == null || !topInventory.equals(mysteriousChestGUI.getInventory()))) {
            return;
        }

        if ((event.getClickedInventory().equals(playerInventory) && (event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_ALL) || event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) || event.getClickedInventory().equals(topInventory) && (event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.PLACE_ONE) || event.getAction().equals(InventoryAction.PLACE_SOME) || event.getAction().equals(InventoryAction.HOTBAR_SWAP)))) {
            event.setCancelled(true);
            return;
        }

        ArrayList<ItemStack> allItems = new ArrayList<>();

        for (ItemStack itemStack : topInventory.getContents()) {
            if (itemStack != null) {
                allItems.add(itemStack);
            }
        }

        ItemStack itemStack = event.getCurrentItem();

        FileConfiguration config = ConfigSystem.getConfig();
        if (topInventory.equals(mysticChestGUI.getInventory()) || topInventory.equals(mysteriousChestGUI.getInventory())) {
            PlaceHolders.addPlaceholder("%player", player.getName());
            PlaceHolders.addPlaceholder("%player%", player.getName());

            if (event.getAction() != InventoryAction.NOTHING && (allItems.size() == 0 || (allItems.size() == 1 && (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) || event.getAction().equals(InventoryAction.DROP_ONE_CURSOR) || event.getAction().equals(InventoryAction.DROP_ALL_SLOT) || event.getAction().equals(InventoryAction.DROP_ONE_SLOT))))) {
                if (mysticChest != null && !mysticClosed) {
                    Bukkit.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mystic_chest_opened_message")));

                    mysticChest.setType(Material.AIR);
                    mysticHologram.delete();
                    mysticHologram = null;
                    mysticChest = null;
                } else if (mysteriousChest != null && !mysteriousClosed) {
                    Bukkit.getServer().broadcastMessage(PlaceHolders.formatText(config.getString("mysterious_chest_opened_message")));

                    mysteriousChest.setType(Material.AIR);
                    mysteriousHologram.delete();
                    mysteriousHologram = null;
                    mysteriousChest = null;
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.equals(mysticChest) || block.equals(mysteriousChest)) {
            event.setCancelled(true);
        }
    }
}
