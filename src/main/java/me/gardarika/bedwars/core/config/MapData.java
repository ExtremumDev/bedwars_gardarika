package me.gardarika.bedwars.core.config;

import me.gardarika.bedwars.core.items.ResourceType;
import me.gardarika.bedwars.core.utils.Coordinates;

import java.util.List;
import java.util.Map;

public class MapData {

    private final String mapId;
    private final TeamConfig[] teams;
    private final Map<ResourceType, List<Coordinates>> resourceSpawners;
    private final Coordinates spectatorsSpawn;
    private final Coordinates waitingSpawn;

    public MapData(
            String mapId,
            TeamConfig[] teams,
            Map<ResourceType, List<Coordinates>> resourceSpawners,
            Coordinates spectatorsSpawn,
            Coordinates waitingSpawn
    ){
        this.mapId = mapId;
        this.resourceSpawners = resourceSpawners;
        this.teams = teams;
        this.spectatorsSpawn = spectatorsSpawn;
        this.waitingSpawn = waitingSpawn;
    }

    public TeamConfig[] getTeams() {
        return teams;
    }

    public Map<ResourceType, List<Coordinates>> getResourceSpawners() {
        return resourceSpawners;
    }

    public Coordinates getSpectatorsSpawn() {
        return spectatorsSpawn;
    }

    public Coordinates getWaitingSpawn() {
        return waitingSpawn;
    }

    public String getMapId() {
        return mapId;
    }
}
