package me.hostadam.generators.generator.impl;

import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.Map;

public class EntityGenerator extends Generator<EntityType> {

    public EntityGenerator(Location location, EntityType toGenerate) {
        super(GeneratorType.ENTITY, location, toGenerate);
    }

    public EntityGenerator(ConfigurationSection section) {
        super(section);
        this.toGenerate = EntityType.valueOf(section.getString("toGenerate"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("toGenerate", this.toGenerate.name());
        return map;
    }

    @Override
    public void generate() {
        this.location.getWorld().spawnEntity(this.location, this.toGenerate);
    }
}
