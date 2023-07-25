package me.hostadam.generators;

import lombok.Getter;
import me.hostadam.generators.generator.GeneratorHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class GeneratorsPlugin extends JavaPlugin {

    @Getter
    private static GeneratorsPlugin instance;

    @Getter
    private GeneratorHandler handler;

    @Override
    public void onEnable() {
        instance = this;

        this.handler = new GeneratorHandler(this);
    }
}