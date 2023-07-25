package me.hostadam.generators.command;

import me.hostadam.generators.GeneratorsPlugin;
import me.hostadam.generators.generator.Generator;
import me.hostadam.generators.generator.GeneratorType;
import me.hostadam.generators.generator.gui.GeneratorEditGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GeneratorCommand extends Command {

    private final GeneratorsPlugin plugin;

    public GeneratorCommand(GeneratorsPlugin plugin) {
        super("generator", "Manage generators", "/generator <add|edit>", Arrays.asList("generators", "gen"));
        this.plugin = plugin;
        this.setPermission("generators.command.admin");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1) {
            sender.sendMessage("§cUsage: /generator <add|edit>");
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou must be a player to perform this command.");
            return true;
        }

        Player player = (Player) sender;

        switch(args[0].toLowerCase()) {
            case "add":
            case "create":
                if(args.length < 3) {
                    sender.sendMessage("§cUsage: /generator add <BLOCK|ITEM|ENTITY> <params>");
                    return true;
                }

                GeneratorType type;
                try {
                    type = GeneratorType.valueOf(args[1].toUpperCase());
                } catch(Exception exception) {
                    sender.sendMessage("§cInvalid generator type: " + StringUtils.join(Arrays.stream(GeneratorType.values()).map(Enum::name).collect(Collectors.toList()), ", ") + ".");
                    return true;
                }

                Generator generator = type.newInstance(player.getLocation(), Arrays.copyOfRange(args, 2, args.length));
                if(generator == null) {
                    player.sendMessage("§cThe generator data was invalid. Try again.");
                    return true;
                }

                this.plugin.getHandler().addGenerator(generator);
                player.sendMessage("§aYou have created a generator with ID §l" + generator.getId() + "§a.");
                return true;
            case "edit":
                if(args.length < 2) {
                    player.sendMessage("§cUsage: /generator edit <id>");
                    return true;
                }

                Optional<Generator> optional = this.plugin.getHandler().getById(args[1]);
                if(optional.isEmpty()) {
                    player.sendMessage("§cA generator with that ID was not found.");
                    return true;
                }

                generator = optional.get();
                player.sendMessage("§aEditing generator §l" + generator.getId() + "§a...");
                new GeneratorEditGUI(player, generator).openForPlayer();
                return true;
            default:
                sender.sendMessage("§cUsage: /generator <add|edit>");
                return true;
        }
    }
}
