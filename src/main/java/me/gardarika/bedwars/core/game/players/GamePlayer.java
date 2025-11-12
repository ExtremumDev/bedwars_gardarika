package me.gardarika.bedwars.core.game.players;

import java.util.UUID;

public class GamePlayer {
    private UUID playerUuid;

    public GamePlayer(UUID playerUuid){
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}
