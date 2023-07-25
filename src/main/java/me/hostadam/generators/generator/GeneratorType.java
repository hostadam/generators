package me.hostadam.generators.generator;

import lombok.AllArgsConstructor;
import me.hostadam.generators.generator.impl.BlockGenerator;
import me.hostadam.generators.generator.impl.EntityGenerator;
import me.hostadam.generators.generator.impl.ItemGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum GeneratorType {

    ITEM,
    BLOCK,
    ENTITY;

    public Generator<?> newInstance(Player player, Location location) {
        switch(this) {
            case ITEM:
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if(itemStack.getType() == Material.AIR) {
                    return null;
                }

                return new ItemGenerator(location, itemStack);
            case ENTITY:
                itemStack = player.getInventory().getItemInMainHand();
                if(itemStack.getType() == Material.AIR) {
                    return null;
                }

                EntityType type = EntityType.valueOf(itemStack.getType().name().replace("_SPAWN_EGG", ""));
                return new EntityGenerator(location, type);
            case BLOCK:
                itemStack = player.getInventory().getItemInMainHand();
                if(itemStack.getType() == Material.AIR) {
                    return null;
                }

                return new BlockGenerator(location, itemStack);
            default:
                return null;
        }
    }
}
