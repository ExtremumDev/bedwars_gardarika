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

    public void setCurrentPlayerGame(Player p, Game game){
        ServerPlayer player = players.get(p.getUniqueId());

        player.setCurrentGame(game);
    }

    @Nullable
    public Game getPlayerCurrentActiveGame(Player p){
        ServerPlayer serverPlayer = players.get(p.getUniqueId());

        return serverPlayer.getCurrentGame();
    }
}
