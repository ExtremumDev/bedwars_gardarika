package me.gardarika.bedwars.core.game;

import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import me.gardarika.bedwars.core.game.team.Team;
import me.gardarika.bedwars.core.game.team.TeamColor;
import me.gardarika.bedwars.listeners.player.PlayerDamageListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    /**
     *
     */
    // Main properties
    private final Arena arena;
    private GameState currentGameState;


    // Game mode properties
    private int maxPlayers;


    // Players section
    private List<Team> teams;
    private List<GamePlayer> players;
    private List<Player> spectators;



    public Game(Arena arena){
        this.arena = arena;

        this.teams = new ArrayList<>();

        for (TeamConfig teamConfig : arena.getMap().getTeams()){
            this.teams.add(
                    new Team(teamConfig, arena.getGameWorld())
            );
        }

        this.currentGameState = GameState.WAITING;
    }

    public void addPlayer(Player p){
        switch (this.currentGameState){
            case WAITING:
            case STARTING:
                if (players.size() < maxPlayers){

                } else {
                    p.sendMessage(Component.text("Arena is full", NamedTextColor.RED));
                    return;
                }
                break;
            case ACTIVE:
            case FINISHED:
                this.spectators.add(p);
                setSpectatorSettingsForPlayer(p);
                teleportPlayerToSpectatorSpawn(p);
        }
    }

    public void playerLeave(Player p){
        GamePlayer gamePlayer = getGamePlayer(p.getUniqueId());
        if ()
    }


    public void startGame(){

    }

    public void handlePlayerDamage(EntityDamageEvent event, Player damagedPlayer){
        switch (currentGameState){
            case WAITING:
            case STARTING:
                event.setCancelled(true);

                if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                    
                }
        }
    }

    public void checkEndGame(){

    }

    private void finishGame(){

    }

    public void endForced(){
        this.kickPlayers();

        this.saveData();

        this.arena.clearArena();
    }

    private void clearGame(){

    }

    private void kickPlayers(){

    }

    private void saveData(){

    }

    private void setSpectatorSettingsForPlayer(Player p){

    }

    private void teleportPlayerToSpectatorSpawn(Player p){

    }

    private GamePlayer getGamePlayer(UUID playerUuid){
        for (GamePlayer gamePlayer : this.players){
            if (gamePlayer.getPlayerUuid().equals(playerUuid)){
                return gamePlayer;
            }
        }
        return null;
    }
}
