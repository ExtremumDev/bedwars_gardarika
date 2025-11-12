package me.gardarika.bedwars.core.config;

import me.gardarika.bedwars.core.items.GameResource;
import me.gardarika.bedwars.core.utils.Coordinates;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapData {

    private final String mapId;
    private final List<TeamConfig> teams;
    private final Map<GameResource, List<Coordinates>> resourceSpawners;
    private final Coordinates spectatorsSpawn;
    private final Coordinates waitingSpawn;

    public MapData(String mapId, List<TeamConfig> teams, Map<GameResource, List<Coordinates>> resourceSpawners){
        this.mapId = mapId;
        this.resourceSpawners = resourceSpawners;
        this.teams = teams;
        this.spectatorsSpawn = null;
        this.waitingSpawn = null;
    }

    public List<TeamConfig> getTeams() {
        return teams;
    }

    public Map<GameResource, List<Coordinates>> getResourceSpawners() {
        return resourceSpawners;
    }

    public Coordinates getSpectatorsSpawn() {
        return spectatorsSpawn;
    }

    public Coordinates getWaitingSpawn() {
        return waitingSpawn;
    }
}
