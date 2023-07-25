package me.hostadam.generators.generator;

import lombok.AllArgsConstructor;
import me.hostadam.generators.generator.impl.BlockGenerator;
import me.hostadam.generators.generator.impl.EntityGenerator;
import me.hostadam.generators.generator.impl.ItemGenerator;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
public enum GeneratorType {

    ITEM(ItemGenerator.class),
    BLOCK(BlockGenerator.class),
    ENTITY(EntityGenerator.class);

    private Class clazz;

    public Generator<?> newInstance(Location location, Object value) {
        try {
            Constructor constructor = this.clazz.getConstructor(Location.class, Object.class);
            return (Generator<?>) constructor.newInstance(location, value);
        } catch (Exception ignored) {
            return null;
        }
    }
}
