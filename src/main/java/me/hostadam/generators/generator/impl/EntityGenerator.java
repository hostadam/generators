package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import org.bukkit.Location;
import org.bukkit.Material;

public class EntityGenerator extends Generator<Material> {

    public EntityGenerator(Location location, Material toGenerate) {
        super(location, toGenerate);
    }

    @Override
    public void generate() {
        this.location.getBlock().setType(this.toGenerate);
    }
}
