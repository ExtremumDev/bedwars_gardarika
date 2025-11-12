package me.gardarika.bedwars.core.player;

import me.gardarika.bedwars.core.game.Game;

import java.util.UUID;

public class ServerPlayer {
    private final UUID playerUuid;
    private Game currentGame = null;

    public ServerPlayer(UUID playerUuid){
        this.playerUuid = playerUuid;
    }

    public void joinGame(Game game){
        this.currentGame = game;
    }

    public Game getCurrentGame(){
        return currentGame;
    }
}
