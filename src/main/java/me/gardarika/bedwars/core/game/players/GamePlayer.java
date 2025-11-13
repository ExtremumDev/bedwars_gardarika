package me.gardarika.bedwars.core.game.players;

import java.util.UUID;

public class GamePlayer {
    private final UUID playerUuid;
    private PlayerState state;

    private boolean isOnArena = true;

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

    public void setPlayerState(PlayerState state){
        this.state = state;
    }

    public boolean isOnArena() {
        return isOnArena;
    }

    public void setOnArena(boolean onArena) {
        isOnArena = onArena;
    }
}
