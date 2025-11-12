package me.gardarika.bedwars.core.game.players;

import java.util.UUID;

public class GamePlayer {
    private UUID playerUuid;
    private PlayerState state;

    public GamePlayer(UUID playerUuid){
        this(playerUuid, PlayerState.ALIVE);
    }

    public GamePlayer(UUID playerUuid, PlayerState playerState){
        this.playerUuid = playerUuid;
        this.state = playerState;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public PlayerState getCurrentState() {
        return state;
    }
}
