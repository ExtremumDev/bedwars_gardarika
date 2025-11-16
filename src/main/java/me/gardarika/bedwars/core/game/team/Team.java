package me.gardarika.bedwars.core.game.team;


import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import me.gardarika.bedwars.core.game.players.PlayerState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final TeamColor teamColor;
    private final Location spawnLocation;
    private final Location bedFootLocation;
    private final Location bedHeadLocation;

    private List<GamePlayer> teamPlayers = new ArrayList<>();

    private boolean hasBed = true;


    public Team(TeamConfig teamConfig, World gameWorld){
        this.teamColor = teamConfig.getTeamColor();
        this.spawnLocation = teamConfig.getSpawnCoordinates().toLocation(gameWorld);
        this.bedFootLocation = teamConfig.getBedFootCoordinates().toLocation(gameWorld);
        this.bedHeadLocation = teamConfig.getBedHeadCoordinates().toLocation(gameWorld);
    }

    public void addPlayer(GamePlayer player){
        player.setTeam(this);
    }

    public boolean isTeamBed(Block bedBlock){
        Location bedBlockLocation = bedBlock.getLocation();

        return bedBlockLocation.equals(bedFootLocation) || bedBlockLocation.equals(bedHeadLocation);
    }

    public boolean hasBed() {
        return hasBed;
    }

    public void destroyBed() {
        this.hasBed = false;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public boolean isLost(){
        boolean lost = true;
        for (GamePlayer gamePlayer : teamPlayers){
            if (!gamePlayer.getCurrentState().equals(PlayerState.LOST)){
                return false;
            }
        }

        return lost;
    }

    public int getTeamSize(){
        return teamPlayers.size();
    }
}
