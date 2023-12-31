package me.hostadam.generators.generator;

import lombok.Getter;
import lombok.Setter;
import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public abstract class Generator<T> implements ConfigurationSerializable {

    private int id;
    protected Location location;
    protected GeneratorType type;
    protected T toGenerate;

    private boolean paused = false;
    private int generations, maxGenerations;

    private long minDelay, maxDelay;
    private long interval, lastGeneration, nextGeneration;
    private long creationDate, duration;
    private BukkitTask task;

    public Generator(GeneratorType type, Location location, T toGenerate) {
        this.type = type;
        this.location = location;
        this.toGenerate = toGenerate;
        this.generations = 0;
        this.minDelay = 0;
        this.maxDelay = 0;
        this.interval = 0;
        this.lastGeneration = 0;
        this.nextGeneration = 0;
        this.duration = 0;
        this.creationDate = System.currentTimeMillis();
    }

    public Generator(ConfigurationSection section) {
        this.type = GeneratorType.valueOf(section.getString("type"));
        this.location = Location.deserialize(section.getConfigurationSection("location").getValues(false));
        this.paused = section.getBoolean("paused");
        this.generations = section.getInt("generations");
        this.maxGenerations = section.getInt("maxGenerations");
        this.minDelay = section.getLong("minDelay");
        this.maxDelay = section.getLong("maxDelay");
        this.interval = section.getLong("interval");
        this.creationDate = section.getLong("creationDate");
        this.duration = section.getLong("duration");
    }

    public void startTask() {
        if(this.task != null) {
            this.task.cancel();
        }

        this.task = Bukkit.getScheduler().runTaskTimer(GeneratorsPlugin.getInstance(), () -> {
            if(this.shouldGenerate()) {
                this.generate();
                if(this.tickValues()) {
                    this.task.cancel();
                    this.task = null;
                    GeneratorsPlugin.getInstance().getHandler().remove(this);
                }
            }
        }, 5, 5);
    }

    public boolean tickValues() {
        this.generations++;
        if(this.maxGenerations != 0 && (this.generations > this.maxGenerations) || (this.duration != 0 && System.currentTimeMillis() - this.creationDate >= this.duration)) {
            return true;
        }

        this.lastGeneration = System.currentTimeMillis();
        this.nextGeneration = this.lastGeneration + (this.interval != 0 ? this.interval : ThreadLocalRandom.current().nextLong(this.minDelay, this.maxDelay));
        return false;
    }

    public boolean shouldGenerate() {
        if(this.location == null || this.toGenerate == null || this.paused) {
            return false;
        }

        if(this.interval != 0) {
            return this.lastGeneration == 0 || System.currentTimeMillis() - this.lastGeneration >= interval;
        }

        if(this.nextGeneration != 0) {
            return this.lastGeneration == 0 || System.currentTimeMillis() - this.nextGeneration >= interval;
        }

        return false;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("type", this.type.name());
        map.put("location", this.location.serialize());
        map.put("paused", this.paused);
        map.put("generations", this.generations);
        map.put("maxGenerations", this.maxGenerations);
        map.put("minDelay", this.minDelay);
        map.put("maxDelay", this.maxDelay);
        map.put("interval", this.interval);
        map.put("creationDate", this.creationDate);
        map.put("duration", this.duration);

        return map;
    }

    public abstract void generate();
}
