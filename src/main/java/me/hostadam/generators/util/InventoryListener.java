package me.hostadam.generators.util;

import me.hostadam.generators.generator.gui.GeneratorGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        GeneratorGUI gui = GeneratorGUI.byPlayer((Player) event.getWhoClicked());
        if(gui != null) {
            gui.click(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        GeneratorGUI.closeForPlayer((Player) event.getPlayer());
    }
}
