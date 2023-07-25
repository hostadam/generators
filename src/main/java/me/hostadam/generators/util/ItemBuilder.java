package me.hostadam.generators.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    public static ItemStack GLASS_PANE = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withName(" ").build();

    private ItemStack itemStack;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.meta = itemStack.getItemMeta();
    }

    public ItemBuilder withName(String name) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder withLoreLine(String loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder withLore(String... loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        for(String line : loreLine) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        this.meta.setLore(lore);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }
}
