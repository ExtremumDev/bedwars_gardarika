package me.gardarika.bedwars.core.game.players;

import me.gardarika.bedwars.core.game.team.Team;

import java.util.UUID;

public class GamePlayer {
    private final UUID playerUuid;
    private PlayerState state;
    private Team team = null;
    private final PlayerGameStatistic statistic;

    private boolean isOnArena = true;

    public GamePlayer(UUID playerUuid, boolean isGameMember){
        this.playerUuid = playerUuid;

        if (isGameMember){
            this.state = PlayerState.ALIVE;
            this.statistic = new PlayerGameStatistic();
        } else {
            this.state = PlayerState.SPECTATOR;
            this.statistic = null;
        }
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

    public void addDestroyedBed(){
        this.statistic.addDestroyedBed();
    }

    public void addKill(boolean isFinal){
        this.statistic.addKill(isFinal);
    }

    public void addDeath(){
        this.statistic.addDeath();
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public Team getTeam(){
        return team;
    }
}
