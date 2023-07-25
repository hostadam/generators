package me.hostadam.generators.generator.task;

import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ListIterator;
public class GeneratorTask extends BukkitRunnable {

    private final GeneratorHandler handler;

    public GeneratorTask(GeneratorHandler handler, int interval) {
        this.handler = handler;
        this.runTaskTimer(GeneratorsPlugin.getInstance(), interval, interval);
    }

    @Override
    public void run() {
        ListIterator<Generator> iterator = this.handler.getGenerators().listIterator();
        while(iterator.hasNext()) {
            Generator generator = iterator.next();
            if(generator.shouldGenerate()) {
                generator.generate();

                if(generator.tickValues()) {
                    this.handler.remove(generator);
                    iterator.remove();
                }
            }
        }
    }
}
