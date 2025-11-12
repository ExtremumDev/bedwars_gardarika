package me.gardarika.bedwars.core.config;

import me.gardarika.bedwars.core.items.GameResource;
import me.gardarika.bedwars.core.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapData {

    private final String mapId;
    private final List<TeamConfig> teams;
    private final Map<GameResource, List<Coordinates>> resourceSpawners;

    public MapData(String mapId, List<TeamConfig> teams, Map<GameResource, List<Coordinates>> resourceSpawners){
        this.mapId = mapId;
        this.resourceSpawners = resourceSpawners;
        this.teams = teams;
    }

    public List<TeamConfig> getTeams() {
        return teams;
    }
}
