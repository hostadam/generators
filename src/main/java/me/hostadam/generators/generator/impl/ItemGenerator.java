package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemGenerator extends Generator<ItemStack> {

    public ItemGenerator(Location location, ItemStack toGenerate) {
        super(GeneratorType.ITEM, location, toGenerate);
    }

    public ItemGenerator(ConfigurationSection section) {
        super(section);
        this.toGenerate = ItemStack.deserialize(section.getConfigurationSection("toGenerate").getValues(false));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("toGenerate", this.toGenerate.serialize());
        return map;
    }

    @Override
    public void generate() {
        this.location.getWorld().dropItem(location, this.toGenerate);
    }
}
