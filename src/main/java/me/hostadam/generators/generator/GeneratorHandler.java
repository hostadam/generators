package me.hostadam.generators.generator;

import com.google.common.primitives.Ints;
import lombok.Getter;
import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.generator.impl.BlockGenerator;
import me.hostadam.generators.generator.impl.EntityGenerator;
import me.hostadam.generators.generator.impl.ItemGenerator;
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

    public GeneratorHandler(GeneratorsPlugin plugin) {
        this.plugin = plugin;

        for(String key : plugin.getConfig().getConfigurationSection("generators").getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("generators." + key);
            if(section == null || !section.contains("type")) continue;
            String type = section.getString("type");
            switch (type) {
                case "BLOCK":
                    BlockGenerator generator = new BlockGenerator(section);
                    this.addGenerator(generator);
                    break;
                case "ENTITY":
                    EntityGenerator entityGenerator = new EntityGenerator(section);
                    this.addGenerator(entityGenerator);
                    break;
                case "ITEM":
                    ItemGenerator itemGenerator = new ItemGenerator(section);
                    this.addGenerator(itemGenerator);
                    break;
            }
        }
    }

    public void remove(Generator<?> generator) {
        //Deduct the id if you remove the latest generator.
        if(generator.getId() == this.id) {
            this.id--;
        }

        this.generators.remove(generator);
        if(this.plugin.getConfig().contains("generators." + generator.getId())) {
            this.plugin.getConfig().set("generators." + generator.getId(), null);
        }

        this.plugin.saveConfig();
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

        if(generator.getTask() == null) {
            generator.startTask();
        }
    }

}
