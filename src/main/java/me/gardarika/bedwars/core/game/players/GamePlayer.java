package me.gardarika.bedwars.core.game.players;

import me.gardarika.bedwars.core.game.team.Team;

import java.util.UUID;

public class GamePlayer {
    private final UUID playerUuid;
    private PlayerState state;
    private Team team;

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

    public void addDestroyedBed(){

    }

    public void addKill(boolean isFinal){

    }

    public void addDeath(){

    }

    public void setTeam(Team team){
        this.team = team;
    }

    public Team getTeam(){
        return team;
    }
}
