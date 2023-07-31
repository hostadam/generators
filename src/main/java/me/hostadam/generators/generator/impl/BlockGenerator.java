package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockGenerator extends Generator<ItemStack> {

    public BlockGenerator(Location location, ItemStack toGenerate) {
        super(GeneratorType.BLOCK, location, toGenerate);
    }

    public BlockGenerator(ConfigurationSection section) {
        super(section);
        this.toGenerate = ItemStack.deserialize(section.getConfigurationSection("toGenerate").getValues(false));
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("toGenerate", this.toGenerate.serialize());
        return map;
    }

    @Override
    public void generate() {
        this.location.getBlock().setType(this.toGenerate.getType());

        if(this.toGenerate.hasBlockData()) {
            this.location.getBlock().setBlockData(this.toGenerate.getBlockData(this.toGenerate.getType()));
        }
    }
}
