package me.gardarika.bedwars.core.game.team;


import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.game.players.GamePlayer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private TeamColor teamColor;
    private Location spawnLocation;
    private Location bedLocation;

    private List<GamePlayer> teamPlayers = new ArrayList<>();


    public Team(TeamConfig teamConfig, World gameWorld){
        this.teamColor = teamConfig.getTeamColor();
        this.spawnLocation = teamConfig.getSpawnCoordinates().toLocation(gameWorld);
        this.bedLocation = teamConfig.getBedCoordinates().toLocation(gameWorld);
    }
}
