package me.gardarika.bedwars.core.config;

import me.gardarika.bedwars.core.game.team.TeamColor;
import me.gardarika.bedwars.core.utils.Coordinates;

public class TeamConfig {
    private final TeamColor teamColor;
    private final Coordinates bedCoordinates;
    private final Coordinates spawnCoordinates;


    public TeamConfig(TeamColor teamColor, Coordinates bedCoordinates, Coordinates spawnCoordinates){
        this.teamColor = teamColor;
        this.bedCoordinates = bedCoordinates;
        this.spawnCoordinates = spawnCoordinates;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public Coordinates getBedCoordinates() {
        return bedCoordinates;
    }

    public Coordinates getSpawnCoordinates() {
        return spawnCoordinates;
    }
}
