package me.gardarika.bedwars.core.game;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.environment.ResourceSpawner;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import me.gardarika.bedwars.core.game.players.PlayerState;
import me.gardarika.bedwars.core.game.scheduler.tasks.CountdownTask;
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
import org.bukkit.inventory.ItemStack;
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
    private final int maxPlayers;
    private final int teamSize;


    // Players section
    private final Team[] teams;
    private final List<GamePlayer> players = new ArrayList<>();

    // Map properties

    private final Location spectatorsSpawn;
    private final Location waitingSpawn;

    private final Map<ResourceType, ResourceSpawner[]> resourceSpawners = new HashMap<>();
    private final Map<ResourceType, Integer> resourceSpawnTasksId = new HashMap<>();

    // Gameplay properties
    private CountdownTask countdownTask;

    private Team winner;



    public Game(Arena arena, int maxPlayers){
        this.arena = arena;
        this.maxPlayers = maxPlayers;

        World gameWorld = arena.getGameWorld();

        MapData mapData = arena.getMap();

        TeamConfig[] teamConfigs = mapData.getTeams();
        this.teams = new Team[teamConfigs.length];

        for (int i = 0; i < teamConfigs.length; i++){
            this.teams[i] = new Team(teamConfigs[i], gameWorld);
        }

        this.teamSize = this.teams.length;

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

        BedWars.getInstance().getLogger().info(
                String.format("[GAME MANAGER] Game was successfully created. Map: %s . Players number: %d", arena.getMap().getMapId(), maxPlayers)
        );
    }

    // life cycle of game

    private void startCountdown(){
        this.currentGameState = GameState.STARTING;

        this.countdownTask = new CountdownTask(this);

        Bukkit.getScheduler().runTaskTimer(
                BedWars.getInstance(),
                this.countdownTask,
                1,
                1
        );
    }

    private void cancelCountdown(){
        this.currentGameState = GameState.WAITING;

        if (this.countdownTask != null){
            this.countdownTask.cancel();
        }
    }

    /**
     * Starting after countdown came out
     */
    public void startGame(){
        this.cancelCountdown();
        this.currentGameState = GameState.ACTIVE;

        this.spreadPlayersAmongTeams();
        this.teleportPlayerToSpawns();

        for (GamePlayer gamePlayer : players){
            Player p = Bukkit.getPlayer(gamePlayer.getPlayerUuid());

            if (p != null) {
                setGameSettingsForPlayer(p);
            } else {
                gamePlayer.setOnArena(false);
            }
        }
        this.startGameMechanics();
    }

    // Check if winner found, if yes - finish game
    private void checkEndGame(){
        if (findWinner()){
            finishGame();
        }
    }

    private void finishGame(){
        // Natural  game finish in case of winner appear
        this.currentGameState = GameState.FINISHED;

        Bukkit.getScheduler().runTaskLater(
                BedWars.getInstance(),
                () -> {
                    this.clearGame();
                },
                20*15
        );
    }

    public void endForced(){
        // Forced game finish, for example when server turned off or admin do it with command

        this.kickPlayers();

        this.saveData();

        this.arena.clearArena();
    }

    private void clearGame(){
        // Start clear from players, save statistics, start arena reload
        this.arena.setClearing();
        this.kickPlayers();
        this.saveData();
        this.arena.clearArena();
    }

    private void startGameMechanics(){
        setupResourceTasks();
    }

    private void stopGameMechanics(){
        cancelResourceTasks();
    }

    public void addPlayer(Player p){
        switch (this.currentGameState){
            case WAITING:
            case STARTING:
                if (players.size() < maxPlayers){
                    this.players.add(
                            new GamePlayer(p.getUniqueId(), this, true)
                    );

                    setWaitingSettingsForPlayer(p);
                    p.teleport(this.waitingSpawn);

                    if (currentGameState.equals(GameState.WAITING)){
                        // check starting
                        if (players.size() >= 2){
                            startCountdown();
                        }
                    } else { // state = STARTING
                        if (players.size() < 2){
                            this.cancelCountdown();
                            this.currentGameState = GameState.WAITING;
                            this.countdownTask.cancel();
                        }
                    }

                    BedWars.getInstance().getPlayerManager().setCurrentPlayerGame(p, this);
                } else {
                    p.sendMessage(Component.text("Arena is full", NamedTextColor.RED));
                    return;
                }
                break;
            case ACTIVE:
                GamePlayer gamePlayer = this.getGamePlayer(p.getUniqueId(), false);

                if (gamePlayer == null){
                    this.addNewSpectator(p);
                } else {
                    gamePlayer.setOnArena(true);

                }
                BedWars.getInstance().getPlayerManager().setCurrentPlayerGame(p, this);
                break;
            case FINISHED:
                this.addNewSpectator(p);
                BedWars.getInstance().getPlayerManager().setCurrentPlayerGame(p, this);
                break;
        }
    }

    public void playerLeave(Player p){
        GamePlayer gamePlayer = getInGamePlayer(p.getUniqueId());
        if (gamePlayer != null){
            switch (this.currentGameState){
                case WAITING:
                    // Remove from players
                    this.players.remove(gamePlayer);
                    break;
                case STARTING:
                    // Remove from players, check if go to waiting
                    this.players.remove(gamePlayer);
                    break;
                case ACTIVE:
                    // If spectator: remove, if lost: set not in game, if alive: add death -> (if dead): checkEndGame set not in game
                    handleActivePlayerQuit(gamePlayer);
                    break;
                case FINISHED:
                    // If spectator: remove, if lost, alive, dead, lost: set not in game
                    if (gamePlayer.getCurrentState().equals(PlayerState.SPECTATOR)){
                        this.players.remove(gamePlayer);
                    } else {
                        gamePlayer.setOnArena(false);
                    }
            }

            BedWars.getInstance().getPlayerManager().setCurrentPlayerGame(p, null);
        }
    }

    private void handleActivePlayerQuit(GamePlayer gamePlayer){
        switch (gamePlayer.getCurrentState()){
            case ALIVE:
                gamePlayer.addDeath();
                gamePlayer.setOnArena(false);
                gamePlayer.setPlayerState(PlayerState.DEAD);
                gamePlayer.stopGameMechanics();
                this.checkEndGame();
                break;
            case DEAD:
                gamePlayer.setOnArena(false);
                this.checkEndGame();
                break;
            case LOST:
                gamePlayer.setOnArena(false);
                break;
            case SPECTATOR:
                this.players.remove(gamePlayer);
                break;
        }
    }

    private void spreadPlayersAmongTeams(){
        this.sortTeamArray();

        int currentSmallestTeam = 0;
        for (GamePlayer gamePlayer : players){
            if (gamePlayer.getTeam() == null){
                Team playerTeam = teams[currentSmallestTeam];

                if (playerTeam.getTeamSize() != teamSize){
                    playerTeam.addPlayer(gamePlayer);

                    // Go to next team, where fewer players
                    if (currentSmallestTeam < teamSize - 1 && playerTeam.getTeamSize() > teams[currentSmallestTeam + 1].getTeamSize()){
                        currentSmallestTeam++;
                    }
                }
            }
        }
    }

    /**
     *
     *
     * @return game winner - if found
     */

    private boolean findWinner(){
        Team winner = null;
        for (Team team : teams){
            if (!team.isLost()){
                if (winner == null){
                    winner = team;
                } else {
                    return false;
                }
            }
        }

        if (winner == null){
            return false;
        } else{
            this.winner = winner;
            return true;
        }
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
                                    if (damagerGamePlayer.getCurrentState().equals(PlayerState.ALIVE)){
                                        if (damagerGamePlayer.getTeam().equals(gamePlayer.getTeam())) {
                                            event.setCancelled(true);
                                            return;
                                        }
                                    } else {
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                        }

                        event.setCancelled(checkPlayerDeath(gamePlayer, damagedPlayer, event.getFinalDamage(), isVoidDamage));


                    } else {
                        event.setCancelled(true);

                        if (isVoidDamage){
                            damagedPlayer.setFlying(true);
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
                                if (!bedTeam.equals(destroyerGamePlayer.getTeam())){
                                    if (bedTeam.hasBed()) {
                                        bedDestroyed(bedTeam, destroyerGamePlayer);
                                        return false;
                                    }
                                } else {
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
        if (isVoid){
            teleportPlayerToSpectatorSpawn(player);
            playerDead(gamePlayer, player);
            return true;
        }
        if (player.getHealth() - damage <= 0){
            playerDead(gamePlayer, player);
            return true;
        }
        return false;
    }

    private void playerDead(GamePlayer deadGamePlayer, Player dead){
        deadGamePlayer.addDeath();
        deadGamePlayer.setPlayerState(PlayerState.DEAD);
        setSpectatorSettingsForPlayer(dead);

        if (!deadGamePlayer.getTeam().hasBed()){
            deadGamePlayer.setPlayerState(PlayerState.LOST);
        } else{
            // Player on revolve
            this.playerOnRevolving(dead, deadGamePlayer);
        }
    }

    private void playerOnRevolving(Player player, GamePlayer gamePlayer){
        setSpectatorSettingsForPlayer(player);
        gamePlayer.startRevolving();
    }

    public void revolvePlayer(GamePlayer player){
        player.setPlayerState(PlayerState.ALIVE);

        Player p = Bukkit.getServer().getPlayer(player.getPlayerUuid());

        if (p != null){
            setGameSettingsForPlayer(p);
            p.teleport(player.getTeam().getSpawnLocation());
        } else {
            player.setOnArena(false);
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

    private void setWaitingSettingsForPlayer(Player p){
        setGameSettingsForPlayer(p);
        p.getInventory().setItem(8, new ItemStack(Material.RED_BED));
    }

    private void setGameSettingsForPlayer(Player p){
        p.setFlying(false);
        p.setAllowFlight(false);
        p.setGameMode(GameMode.SURVIVAL);

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

    @Nullable
    private GamePlayer getInGamePlayer(UUID playerUuid){
        return getGamePlayer(playerUuid, true);
    }

    /**
     *
     * @param checkOnArena - if true - return only if player on arena, if false - return in any case
     * @return
     */
    @Nullable
    private GamePlayer getGamePlayer(UUID playerUuid, boolean checkOnArena){
        for (GamePlayer gamePlayer : this.players){
            if (gamePlayer.getPlayerUuid().equals(playerUuid) && (gamePlayer.isOnArena() || !(checkOnArena))){
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
                    0,
                    resourceType.getDefaultSpawnInterval()
            );
            this.resourceSpawnTasksId.put(resourceType, spawnTask.getTaskId());
        }
    }

    private void cancelResourceTasks(){
        for (ResourceType resourceType : this.resourceSpawnTasksId.keySet()){
            Bukkit.getScheduler().cancelTask(this.resourceSpawnTasksId.get(resourceType));
        }
    }

    private void addNewSpectator(Player p){
        this.players.add(
                new GamePlayer(p.getUniqueId(), this, false)
        );

        this.setSpectatorSettingsForPlayer(p);
        this.teleportPlayerToSpectatorSpawn(p);
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

    private void sortTeamArray(){
        Team peTeam;
        for (int i = 1; i < teams.length; i++){
            for (int j = i; j < teams.length; j++){
                if (teams[j].getTeamSize() < teams[0].getTeamSize()){
                    peTeam = teams[j];
                    teams[j] = teams[0];
                    teams[j] = peTeam;
                }
            }
        }
    }

    private void teleportPlayerToSpawns(){
        for (Team team : teams){
            team.teleportPlayersToTeamSpawn();
        }
    }
    public Arena getArena() {
        return arena;
    }
}
