package me.hostadam.generators.generator.gui;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.hostadam.generators.GeneratorsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class GeneratorGUI {

    private static Map<UUID, GeneratorGUI> GUIS = new HashMap<>();

    protected final GeneratorsPlugin plugin = GeneratorsPlugin.getInstance();

    @Getter
    @NonNull protected Player player;
    @Setter
    protected Inventory inventory;

    public void openForPlayer() {
        this.open();
        if(this.inventory == null) return;
        GUIS.put(player.getUniqueId(), this);
    }

    public static void closeForPlayer(Player player) {
        GeneratorGUI generatorGUI = GUIS.get(player.getUniqueId());
        if(generatorGUI != null && generatorGUI.close(player)) {
            GUIS.remove(player.getUniqueId());
        }
    }

    public abstract void open();

    public abstract boolean close(Player player);
    public abstract void click(InventoryClickEvent event);
    public abstract void drag(InventoryDragEvent event);

    public static GeneratorGUI byPlayer(Player player) {
        return GUIS.get(player.getUniqueId());
    }

}
