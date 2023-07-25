package me.hostadam.generators.generator;

import lombok.AllArgsConstructor;
import me.hostadam.generators.generator.impl.BlockGenerator;
import me.hostadam.generators.generator.impl.EntityGenerator;
import me.hostadam.generators.generator.impl.ItemGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum GeneratorType {

    ITEM,
    BLOCK,
    ENTITY;

    public Generator<?> newInstance(Location location, String[] args) {
        switch(this) {
            case ITEM:
                ItemStack itemStack = new ItemStack(Material.valueOf(args[0]));
                return new ItemGenerator(location, itemStack);
            case ENTITY:
                return new EntityGenerator(location, EntityType.valueOf(args[0]));
            case BLOCK:
                return new BlockGenerator(location, Material.valueOf(args[0]));
            default:
                return null;
        }
    }
}
