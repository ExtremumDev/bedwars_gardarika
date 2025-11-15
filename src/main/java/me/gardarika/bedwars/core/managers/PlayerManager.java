package me.gardarika.bedwars.core.managers;

import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.player.ServerPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private Map<UUID, ServerPlayer> players = new HashMap<>();

    public PlayerManager(){

    }

    public void newPlayer(Player player){
        players.put(
                player.getUniqueId(),
                new ServerPlayer(player.getUniqueId())
        );
    }

    @Nullable
    public Game getPlayerCurrentActiveGame(Player p){
        ServerPlayer serverPlayer = players.get(p.getUniqueId());

        return serverPlayer.getCurrentGame();
    }
}
