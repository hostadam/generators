package me.hostadam.generators.util;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        Input input = Input.byPlayer(player);
        if(input != null) {
            event.setCancelled(true);
            input.getResponseRequest().accept(message);
        }
    }

    @Getter
    public static class Input {

        private static final Map<UUID, Input> INPUTS = new HashMap<>();

        private final Player player;
        private final Consumer<String> responseRequest;

        private Input(Player player, Consumer<String> responseRequest) {
            this.player = player;
            this.responseRequest = responseRequest;
        }

        public static void start(Player player, Consumer<String> task) {
            INPUTS.put(player.getUniqueId(), new Input(player, task));
        }

        public static Input byPlayer(Player player) {
            return INPUTS.get(player.getUniqueId());
        }
    }
}
