package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class BlockGenerator extends Generator<Material> {

    public BlockGenerator(Location location, Material toGenerate) {
        super(GeneratorType.BLOCK, location, toGenerate);
    }

    public BlockGenerator(ConfigurationSection section) {
        super(section);
        this.toGenerate = Material.valueOf(section.getString("toGenerate"));
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("toGenerate", this.toGenerate.name());
        return map;
    }

    @Override
    public void load(ConfigurationSection section) {
        this.toGenerate = Material.valueOf(section.getString("toGenerate"));
    }

    @Override
    public void generate() {
        this.location.getBlock().setType(this.toGenerate);
    }
}
