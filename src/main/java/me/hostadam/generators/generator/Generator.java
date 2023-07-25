package me.hostadam.generators.generator;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public abstract class Generator<T> {

    protected Location location;
    protected T toGenerate;


    private int generations, maxGenerations;
    private long interval, nextGeneration;
    private long creationDate, lastGeneration, duration;

    public Generator(Location location, T toGenerate) {
        this.location = location;
        this.toGenerate = toGenerate;
        this.creationDate = System.currentTimeMillis();
    }

    public boolean tickValues() {
        this.generations++;
        return this.generations > this.maxGenerations || System.currentTimeMillis() - this.creationDate >= this.duration;
    }

    public boolean shouldGenerate() {
        if(this.location == null || this.toGenerate == null) {
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

    public abstract void generate();
}
