package me.gardarika.bedwars.core.game.players;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.game.scheduler.tasks.RevolvingTask;
import me.gardarika.bedwars.core.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GamePlayer {
    private final Game game;
    private final UUID playerUuid;
    private PlayerState state;
    private Team team = null;
    private final PlayerGameStatistic statistic;

    private boolean isOnArena = true;

    private int secondsUntilRevolving = 0;
    private int revolvingTaskId;

    public GamePlayer(UUID playerUuid, Game game, boolean isGameMember){
        this.playerUuid = playerUuid;
        this.game = game;

        if (isGameMember){
            this.state = PlayerState.ALIVE;
            this.statistic = new PlayerGameStatistic();
        } else {
            this.state = PlayerState.SPECTATOR;
            this.statistic = null;
        }
    }

    public void startRevolving(){
        this.secondsUntilRevolving = 5;
        BukkitRunnable task = new RevolvingTask(this);

        task.runTaskTimer(BedWars.getInstance(), 0, 20);


        this.revolvingTaskId = task.getTaskId();
    }

    public void revolve(){
        this.game.revolvePlayer(this);
        Bukkit.getScheduler().cancelTask(this.revolvingTaskId);
    }

    public void stopGameMechanics(){
        if (state.equals(PlayerState.DEAD)){
            if (this.revolvingTaskId != 0){
                Bukkit.getScheduler().cancelTask(this.revolvingTaskId);
            }
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

    public Game getGame() {
        return game;
    }
}
