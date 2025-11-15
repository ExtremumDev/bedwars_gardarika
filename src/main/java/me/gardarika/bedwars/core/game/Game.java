package me.gardarika.bedwars.core.game;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.environment.ResourceSpawner;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import me.gardarika.bedwars.core.game.players.PlayerState;
import me.gardarika.bedwars.core.game.team.Team;
import me.gardarika.bedwars.core.items.ResourceType;
import me.gardarika.bedwars.core.managers.LobbyManager;
import me.gardarika.bedwars.core.utils.Coordinates;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.*;

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
    private final Team[] teams;
    private List<GamePlayer> players;

    // Map properties

    private final Location spectatorsSpawn;
    private final Location waitingSpawn;

    private final Map<ResourceType, ResourceSpawner[]> resourceSpawners = new HashMap<>();
    private final Map<ResourceType, Integer> resourceSpawnTasksId = new HashMap<>();

    // Gameplay properties

    private Team winner;



    public Game(Arena arena){
        this.arena = arena;

        World gameWorld = arena.getGameWorld();

        MapData mapData = arena.getMap();

        TeamConfig[] teamConfigs = mapData.getTeams();
        this.teams = new Team[teamConfigs.length];

        for (int i = 0; i < teamConfigs.length; i++){
            this.teams[i] = new Team(teamConfigs[i], gameWorld);
        }

        this.currentGameState = GameState.WAITING;

        this.waitingSpawn = mapData.getWaitingSpawn().toLocation(gameWorld);
        this.spectatorsSpawn = mapData.getSpectatorsSpawn().toLocation(gameWorld);


        Map<ResourceType, List<Coordinates>> resourceSpawnersData = mapData.getResourceSpawners();
        for (ResourceType resourceType : resourceSpawnersData.keySet()){
            ResourceSpawner[] typeResourceSpawners = new ResourceSpawner[resourceSpawnersData.get(resourceType).size()];

            for (int j = 0; j < typeResourceSpawners.length; j++){

                typeResourceSpawners[j] = new ResourceSpawner(
                        resourceType,
                        resourceSpawnersData.get(resourceType).get(j).toLocation(gameWorld)
                );
            }

            this.resourceSpawners.put(
                    resourceType,
                    typeResourceSpawners
            );
        }
    }

    // life cycle of game

    private void startCountdown(){
        this.currentGameState = GameState.STARTING;

    }

    private void cancelCountdown(){
        this.currentGameState = GameState.WAITING;
    }


    public void startGame(){

    }

    // Check if winner found, if yes - finish game
    private void checkEndGame(){

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

    private void stopGameMechanics(){
        cancelResourceTasks();
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
        GamePlayer gamePlayer = getInGamePlayer(p.getUniqueId());
        if ()
    }

    private void spreadPlayersAmongTeams(){

    }

    /**
     *
     *
     * @return game winner - if found
     */

    @Nullable
    private Team findWinner(){

    }

    public void handlePlayerDamage(EntityDamageEvent event, Player damagedPlayer){
        boolean isVoidDamage = event.getCause().equals(EntityDamageEvent.DamageCause.VOID);
        switch (currentGameState){
            case WAITING:
            case STARTING:
                event.setCancelled(true);
                if (isVoidDamage){
                    damagedPlayer.teleport(this.waitingSpawn);
                }
                break;
            case FINISHED:
                event.setCancelled(true);

                if (isVoidDamage){

                    GamePlayer gamePlayer = getInGamePlayer(damagedPlayer.getUniqueId());

                    if (gamePlayer != null){
                        if (gamePlayer.getCurrentState().equals(PlayerState.ALIVE)){
                            damagedPlayer.teleport(gamePlayer.getTeam().getSpawnLocation());
                        } else{
                            teleportPlayerToSpectatorSpawn(damagedPlayer);
                        }
                    }
                }
                break;
            case ACTIVE:
                GamePlayer gamePlayer = getInGamePlayer(damagedPlayer.getUniqueId());
                if (gamePlayer != null){
                    if (gamePlayer.getCurrentState().equals(PlayerState.ALIVE)){
                        if (event instanceof EntityDamageByEntityEvent damagedByEntityEvent){

                            if (damagedByEntityEvent.getDamager() instanceof Player){
                                Player damager = (Player) damagedByEntityEvent.getEntity();

                                GamePlayer damagerGamePlayer = getInGamePlayer(damager.getUniqueId());

                                if (damagerGamePlayer != null) {
                                    if (damagerGamePlayer.getTeam().equals(gamePlayer.getTeam())) {
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                        }


                    } else {
                        event.setCancelled(true);

                        if (isVoidDamage){
                            teleportPlayerToSpectatorSpawn(damagedPlayer);
                        }
                    }
                }else {
                    event.setCancelled(true);
                }
        }
    }

    public boolean handleBlockDestroy(Block destroyedBlock, @Nullable Player destroyer){

        switch (this.currentGameState){
            case WAITING:
            case STARTING:
            case FINISHED:
                return true;
            case ACTIVE:
                if(destroyedBlock.getType().toString().endsWith("_BED")){
                    if (destroyer != null){
                        GamePlayer destroyerGamePlayer = getInGamePlayer(destroyer.getUniqueId());

                        if (destroyerGamePlayer != null && destroyerGamePlayer.getCurrentState().equals(PlayerState.ALIVE)){

                            Team bedTeam = findTeamBed(destroyedBlock);

                            if (bedTeam != null){
                                if (bedTeam.hasBed()) {
                                    bedDestroyed(bedTeam, destroyerGamePlayer);
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }

        }

        return true;
    }

    private void bedDestroyed(Team destroyedBedTeam, GamePlayer destroyer){
        destroyedBedTeam.destroyBed();

        destroyer.addDestroyedBed();


    }

    /**
     * @return If player dead
     * **/
    private boolean checkPlayerDeath(GamePlayer gamePlayer, Player player, double damage, boolean isVoid){
        if (player.getHealth() - damage <= 0){
            gamePlayer.addDeath();
            gamePlayer.setPlayerState(PlayerState.DEAD);
            if (isVoid){
                teleportPlayerToSpectatorSpawn(player);
            }

            if (!gamePlayer.getTeam().hasBed()){
                gamePlayer.setPlayerState(PlayerState.LOST);
                setSpectatorSettingsForPlayer(player);
            } else{
                // Player on revolve


            }
        }
        return false;
    }

    private void revolvePlayer(GamePlayer player){
        player.setPlayerState(PlayerState.ALIVE);

        Player p = Bukkit.getServer().getPlayer(player.getPlayerUuid());

        if (p != null){
            setGameSettings(p);
            p.teleport(player.getTeam().getSpawnLocation());
        }

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

    private void setGameSettings(Player p){
        p.setFlying(false);
        p.setAllowFlight(false);

        p.setHealth(20);
        p.setFoodLevel(20);
        p.getInventory().clear();

    }

    private void setSpectatorSettingsForPlayer(Player p){
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.setFireTicks(0);

        p.clearActivePotionEffects();
    }

    private void teleportPlayerToSpectatorSpawn(Player p){
        p.teleport(this.spectatorsSpawn);
    }

    private GamePlayer getInGamePlayer(UUID playerUuid){
        for (GamePlayer gamePlayer : this.players){
            if (gamePlayer.getPlayerUuid().equals(playerUuid)){
                return gamePlayer;
            }
        }
        return null;
    }

    private void spawnResourceForType(ResourceType resourceType){
        for (ResourceSpawner spawner : this.resourceSpawners.get(resourceType)){
            spawner.dropResource();
        }
    }

    private void setupResourceTasks(){
        for (ResourceType resourceType : this.resourceSpawners.keySet()){
            BukkitTask spawnTask = Bukkit.getScheduler().runTaskTimer(
                    BedWars.getInstance(),
                    () -> this.spawnResourceForType(resourceType),
                    20,
                    20
            );
            this.resourceSpawnTasksId.put(resourceType, spawnTask.getTaskId());
        }
    }

    private void cancelResourceTasks(){
        for (ResourceType resourceType : this.resourceSpawnTasksId.keySet()){
            Bukkit.getScheduler().cancelTask(this.resourceSpawnTasksId.get(resourceType));
        }
    }

    @Nullable
    private Team findTeamBed(Block bedBlock){
        Team bedTeam = null;
        for (Team team : this.teams){
            if (team.isTeamBed(bedBlock)){
                bedTeam = team;
                break;
            }
        }

        return bedTeam;
    }
}
