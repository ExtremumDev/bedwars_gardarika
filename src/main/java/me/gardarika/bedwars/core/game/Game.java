package me.gardarika.bedwars.core.game;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import me.gardarika.bedwars.core.game.players.PlayerState;
import me.gardarika.bedwars.core.game.team.Team;
import me.gardarika.bedwars.core.game.team.TeamColor;
import me.gardarika.bedwars.core.managers.LobbyManager;
import me.gardarika.bedwars.core.utils.Coordinates;
import me.gardarika.bedwars.listeners.player.PlayerDamageListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
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

    // Map propetries

    private final Location spectatorsSpawn;
    private final Location waitingSpawn;



    public Game(Arena arena){
        this.arena = arena;

        this.teams = new ArrayList<>();

        for (TeamConfig teamConfig : arena.getMap().getTeams()){
            this.teams.add(
                    new Team(teamConfig, arena.getGameWorld())
            );
        }

        this.currentGameState = GameState.WAITING;

        this.waitingSpawn = arena.getMap().getWaitingSpawn().toLocation(arena.getGameWorld());
        this.spectatorsSpawn = arena.getMap().getSpectatorsSpawn().toLocation(arena.getGameWorld());
    }


    public void startGame(){

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

    public void handlePlayerDamage(EntityDamageEvent event, Player damagedPlayer){
        switch (currentGameState){
            case WAITING:
            case STARTING:
                event.setCancelled(true);
                if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                    damagedPlayer.teleport(this.waitingSpawn);
                }
        }
    }

    public void checkEndGame(){

    }

    private void finishGame(){
        // Natural  game finish in case of winner appear
    }

    public void endForced(){
        // Forced game finish, for example when server turned off or admin do it with command
        this.kickPlayers();

        this.saveData();

        this.arena.clearArena();
    }

    private void clearGame(){
        // Start clear from players, save statistics, start arena reload
    }

    private void kickPlayers(){
        // Clear arena from players, move them to lobby

        LobbyManager lobbyManager = BedWars.getInstance().getLobbyManager();
        for (GamePlayer gamePlayer : this.players){
            if (gamePlayer.isOnArena()){
                lobbyManager.movePlayerToLobby(Bukkit.getPlayer(gamePlayer.getPlayerUuid()));
                gamePlayer.setOnArena(false);
            }

            // Extra check for
            for (Player worldPlayer : arena.getGameWorld().getPlayers()){
                lobbyManager.movePlayerToLobby(worldPlayer);
            }
        }
    }

    private void saveData(){
        // Save player's statistics, game's statistics
    }

    private void setSpectatorSettingsForPlayer(Player p){
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.setFireTicks(0);
    }

    private void teleportPlayerToSpectatorSpawn(Player p){
        p.teleport(this.spectatorsSpawn);
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
