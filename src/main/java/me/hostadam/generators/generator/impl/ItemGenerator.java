package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ItemGenerator extends Generator<ItemStack> {

    public ItemGenerator(Location location, ItemStack toGenerate) {
        super(location, toGenerate);
    }

    @Override
    public void generate() {
        this.location.getWorld().dropItem(location, this.toGenerate);
    }
}
