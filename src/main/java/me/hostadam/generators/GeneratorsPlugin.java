package me.hostadam.generators;

import lombok.Getter;
import me.hostadam.generators.command.GeneratorCommand;
import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorHandler;
import me.hostadam.generators.util.ChatListener;
import me.hostadam.generators.util.InventoryListener;
import org.bukkit.configuration.file.FileConfiguration;
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

        getServer().getCommandMap().register("generator", new GeneratorCommand(this));

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        FileConfiguration configuration = this.getConfig();
        for(Generator generator : this.handler.getGenerators()) {
            configuration.set("generators." + generator.getId(), generator.serialize());
        }

        this.saveConfig();
    }
}