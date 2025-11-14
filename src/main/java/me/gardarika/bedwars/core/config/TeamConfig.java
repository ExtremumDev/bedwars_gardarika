package me.gardarika.bedwars.core.config;

import me.gardarika.bedwars.core.game.team.TeamColor;
import me.gardarika.bedwars.core.utils.Coordinates;

public class TeamConfig {
    private final TeamColor teamColor;
    private final Coordinates bedFootCoordinates;
    private final Coordinates bedHeadCoordinates;
    private final Coordinates spawnCoordinates;


    public TeamConfig(
            TeamColor teamColor,
            Coordinates bedFootCoordinates,
            Coordinates bedHeadCoordinates,
            Coordinates spawnCoordinates
    ){
        this.teamColor = teamColor;
        this.bedFootCoordinates = bedFootCoordinates;
        this.bedHeadCoordinates = bedHeadCoordinates;
        this.spawnCoordinates = spawnCoordinates;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public Coordinates getBedFootCoordinates() {
        return bedFootCoordinates;
    }

    public Coordinates getSpawnCoordinates() {
        return spawnCoordinates;
    }

    public Coordinates getBedHeadCoordinates() {
        return bedHeadCoordinates;
    }
}
