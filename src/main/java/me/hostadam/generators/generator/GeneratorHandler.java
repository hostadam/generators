package me.hostadam.generators.generator;

import lombok.Getter;
import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.generator.task.GeneratorTask;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneratorHandler {

    private final GeneratorsPlugin plugin;
    @Getter
    private List<Generator> generators = new ArrayList<>();
    private GeneratorTask task;

    public GeneratorHandler(GeneratorsPlugin plugin) {
        this.plugin = plugin;
        this.task = new GeneratorTask(this, 20);
    }

    public Optional<Generator> getByLocation(Location location) {
        return this.generators.stream().filter(generator -> location.equals(generator.getLocation())).findAny();
    }

    public void addGenerator(Generator generator) {
        this.generators.add(generator);
    }

}
