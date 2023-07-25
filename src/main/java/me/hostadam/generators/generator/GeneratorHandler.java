package me.hostadam.generators.generator;

import com.google.common.primitives.Ints;
import lombok.Getter;
import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.generator.impl.BlockGenerator;
import me.hostadam.generators.generator.impl.EntityGenerator;
import me.hostadam.generators.generator.impl.ItemGenerator;
import me.hostadam.generators.generator.task.GeneratorTask;
import org.bukkit.configuration.ConfigurationSection;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneratorHandler {

    private final GeneratorsPlugin plugin;
    @Getter
    private List<Generator> generators = new ArrayList<>();
    private int id;
    private GeneratorTask task;

    public GeneratorHandler(GeneratorsPlugin plugin) {
        this.plugin = plugin;

        for(String key : plugin.getConfig().getConfigurationSection("generators").getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("generators." + key);
            if(section == null || !section.contains("type")) continue;
            String type = section.getString("type");
            switch (type) {
                case "BLOCK" -> {
                    BlockGenerator generator = new BlockGenerator(section);
                    this.generators.add(generator);
                }
                case "ENTITY" -> {
                    EntityGenerator entityGenerator = new EntityGenerator(section);
                    this.generators.add(entityGenerator);
                }
                case "ITEM" -> {
                    ItemGenerator itemGenerator = new ItemGenerator(section);
                    this.generators.add(itemGenerator);
                }
            }
        }

        this.task = new GeneratorTask(this, 20);
    }

    public Optional<Generator> getByLocation(Location location) {
        return this.generators.stream().filter(generator -> location.equals(generator.getLocation())).findAny();
    }

    public Optional<Generator> getById(String arg) {
        if(Ints.tryParse(arg) == null) {
            return Optional.empty();
        }

        int id = Integer.parseInt(arg);
        return this.generators.stream().filter(generator -> id == generator.getId()).findAny();
    }

    public void addGenerator(Generator<?> generator) {
        ++this.id;
        generator.setId(this.id);
        this.generators.add(generator);
    }

}
