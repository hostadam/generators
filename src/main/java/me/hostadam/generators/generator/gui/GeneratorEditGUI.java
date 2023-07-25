package me.hostadam.generators.generator.gui;

import com.google.common.primitives.Ints;
import lombok.NonNull;
import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.util.ChatListener;
import me.hostadam.generators.util.ItemBuilder;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class GeneratorEditGUI extends GeneratorGUI {

    private Generator generator;

    public GeneratorEditGUI(@NonNull Player player, Generator generator) {
        super(player);
        this.generator = generator;
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(null, 27, "§8Generator Editor");

        this.inventory.setItem(10, new ItemBuilder(Material.NAME_TAG).withName("§e§lMax Generations").withLore("§7To change how many times this generator§7", "§7should generate before disappearing, §fclick here§7.", " ", "§8→ §eCurrent§7: §f" + (this.generator.getMaxGenerations() <= 0 ? "§aUnlimited" : this.generator.getMaxGenerations())).build());
        this.inventory.setItem(11, new ItemBuilder(Material.REPEATER).withName("§e§lInterval").withLore("§7To change how often the generator should", "§7generate, §fclick here §7and enter in chat (in ticks).", " ", "§8→ §eCurrent§7: §f" + DurationFormatUtils.formatDurationWords(this.generator.getInterval(), true, true)).build());
        this.inventory.setItem(12, new ItemBuilder(Material.CLOCK).withName("§e§lMin Delay").withLore("§7To change the lowest delay until the next generation", "§7§fclick here §7and enter in chat (in ticks).", " ", "§8→ §eCurrent§7: §f" + DurationFormatUtils.formatDurationWords(this.generator.getMinDelay(), true, true)).build());
        this.inventory.setItem(13, new ItemBuilder(Material.CLOCK).withName("§e§lMax Delay").withLore("§7To change the max delay until the next generation", "§7§fclick here §7and enter in chat (in ticks).", " ", "§8→ §eCurrent§7: §f" + DurationFormatUtils.formatDurationWords(this.generator.getMaxDelay(), true, true)).build());
        this.inventory.setItem(14, new ItemBuilder(Material.CLOCK).withName("§e§lDuration").withLore("§7To change how long until the generator", "§7should remove itself, §fclick here §7and enter in chat (in ticks).", " ", "§8→ §eCurrent§7: §f" + DurationFormatUtils.formatDurationWords(this.generator.getDuration(), true, true)).build());
        this.inventory.setItem(15, new ItemBuilder(Material.OBSIDIAN).withName("§6§lPause").withLore("§7Do you want to pause the generator?", "§fClick here §7to toggle.", " ", "§8→ §eCurrent§7: " + (this.generator.isPaused() ? "§cPaused" : "§aActive")).build());
        this.inventory.setItem(16, new ItemBuilder(Material.BARRIER).withName("§c§lDelete").withLore("§7Do you want to delete the generator?", "§fClick here §7to delete it.").build());

        for(int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack itemStack = this.inventory.getItem(slot);
            if(itemStack == null || itemStack.getType() == Material.AIR) {
                this.inventory.setItem(slot, ItemBuilder.GLASS_PANE);
            }
        }

        player.openInventory(this.inventory);
    }

    @Override
    public boolean close(Player player) {
        return true;
    }

    @Override
    public void click(InventoryClickEvent event) {
        event.setCancelled(true);

        switch(event.getRawSlot()) {
            case 10:
                player.closeInventory();
                player.sendTitle("§a§lMAX GENERATIONS", "§fType a number in chat", 20, 60, 20);

                ChatListener.Input.start(player, generationsString -> {
                    if(Ints.tryParse(generationsString) == null) {
                        player.sendMessage("§c" + generationsString + " is not a valid number.");
                        return;
                    }

                    int maxGenerations = Integer.parseInt(generationsString);
                    if(maxGenerations < 0) {
                        player.sendMessage("§cThe max generations must be higher than 0.");
                        return;
                    }

                    this.generator.setMaxGenerations(maxGenerations);
                    player.sendMessage("§eYou have set the max generations of §f" + this.generator.getId() + " §eto §f" + maxGenerations + "§e.");
                    Bukkit.getScheduler().runTask(this.plugin, this::openForPlayer);
                });

                break;
            case 11:
                player.closeInventory();
                player.sendTitle("§a§lSPAWN INTERVAL", "§fType a time in chat (1 second = 20 ticks)", 20, 60, 20);

                ChatListener.Input.start(player, spawnIntervalString -> {
                    if(spawnIntervalString.equalsIgnoreCase("reset")) {
                        this.generator.setInterval(0);
                        player.sendMessage("§aYou have reset the interval of this generator.");
                        return;
                    }

                    if(Ints.tryParse(spawnIntervalString) == null) {
                        player.sendMessage("§c" + spawnIntervalString + " is not a valid number.");
                        return;
                    }

                    int intervalTicks = Integer.parseInt(spawnIntervalString);
                    if(intervalTicks < 20) {
                        player.sendMessage("§cThe interval ticks must be higher than 20 (1 second).");
                        return;
                    }

                    this.generator.setInterval(intervalTicks * 50L);
                    player.sendMessage("§eYou have set the interval of §f" + this.generator.getId() + " §eto §f" + intervalTicks + " ticks§e.");
                    Bukkit.getScheduler().runTask(this.plugin, this::openForPlayer);
                });

                break;
            case 12:
                player.closeInventory();
                player.sendTitle("§a§lMIN SPAWN DELAY", "§fType a time in chat (1 second = 20 ticks)", 20, 60, 20);

                ChatListener.Input.start(player, minDelayString -> {
                    if(minDelayString.equalsIgnoreCase("reset")) {
                        this.generator.setMinDelay(0);
                        this.generator.setMaxDelay(0);
                        player.sendMessage("§aYou have reset the spawn delays of this generator.");
                        return;
                    }

                    if(Ints.tryParse(minDelayString) == null) {
                        player.sendMessage("§c" + minDelayString + " is not a valid number.");
                        return;
                    }

                    int minDelayTicks = Integer.parseInt(minDelayString);
                    if(minDelayTicks < 20) {
                        player.sendMessage("§cThe delay ticks must be higher than 20 (1 second).");
                        return;
                    }

                    this.generator.setMinDelay(minDelayTicks * 50L);
                    if(this.generator.getMinDelay() >= this.generator.getMaxDelay()) this.generator.setMaxDelay(this.generator.getMinDelay() + 1000L);
                    player.sendMessage("§eYou have set the min delay of §f" + this.generator.getId() + " §eto §f" + minDelayTicks + " ticks§e.");
                    Bukkit.getScheduler().runTask(this.plugin, this::openForPlayer);
                });

                break;
            case 13:
                player.closeInventory();
                player.sendTitle("§a§lMAX SPAWN DELAY", "§fType a time in chat (1 second = 20 ticks)", 20, 60, 20);

                ChatListener.Input.start(player, maxDelayString -> {
                    if(maxDelayString.equalsIgnoreCase("reset")) {
                        this.generator.setMinDelay(0);
                        this.generator.setMaxDelay(0);
                        player.sendMessage("§aYou have reset the spawn delays of this generator.");
                        return;
                    }

                    if(Ints.tryParse(maxDelayString) == null) {
                        player.sendMessage("§c" + maxDelayString + " is not a valid number.");
                        return;
                    }

                    int maxDelayTicks = Integer.parseInt(maxDelayString);
                    if(maxDelayTicks < 20 || maxDelayTicks <= this.generator.getMinDelay()) {
                        player.sendMessage("§cThe delay ticks must be higher than 20 (1 second).");
                        return;
                    }

                    this.generator.setMaxDelay(maxDelayTicks * 50L);
                    if(this.generator.getMinDelay() >= this.generator.getMaxDelay()) this.generator.setMinDelay(this.generator.getMaxDelay() - 1000L);
                    player.sendMessage("§eYou have set the max delay of §f" + this.generator.getId() + " §eto §f" + maxDelayTicks + " ticks§e.");
                    Bukkit.getScheduler().runTask(this.plugin, this::openForPlayer);
                });

                break;
            case 14:
                player.closeInventory();
                player.sendTitle("§a§lDURATION", "§fType a time in chat (1 second = 20 ticks)", 20, 60, 20);

                ChatListener.Input.start(player, durationString -> {
                    if(durationString.equalsIgnoreCase("reset")) {
                        this.generator.setDuration(0);
                        player.sendMessage("§aYou have reset the duration of this generator.");
                        return;
                    }

                    if(Ints.tryParse(durationString) == null) {
                        player.sendMessage("§c" + durationString + " is not a valid number.");
                        return;
                    }

                    int durationTicks = Integer.parseInt(durationString);
                    if(durationTicks < 20) {
                        player.sendMessage("§cThe duration must be higher than 20 (1 second).");
                        return;
                    }

                    this.generator.setDuration(durationTicks * 50L);
                    player.sendMessage("§eYou have set the duration of §f" + this.generator.getId() + " §eto §f" + durationTicks + " ticks§e.");
                    Bukkit.getScheduler().runTask(this.plugin, this::openForPlayer);
                });

                break;
            case 15:
                this.generator.setPaused(!this.generator.isPaused());
                this.inventory.setItem(15, new ItemBuilder(Material.OBSIDIAN).withName("§6§lPause").withLore("§7Do you want to pause the generator?", "§fClick here §7to toggle.", " ", "§8→ §eCurrent§7: " + (this.generator.isPaused() ? "§aPaused" : "§cActive")).build());
                break;
            case 16:
                plugin.getHandler().getGenerators().remove(this.generator);
                player.sendMessage("§cRemoved a generator.");
                player.closeInventory();
                break;
            default:
                break;

        }
    }

    @Override
    public void drag(InventoryDragEvent event) {
        if(event.getInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }
}
