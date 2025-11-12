package me.gardarika.bedwars.core.managers;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.config.TeamConfig;
import me.gardarika.bedwars.core.game.team.TeamColor;
import me.gardarika.bedwars.core.items.GameResource;
import me.gardarika.bedwars.core.utils.Coordinates;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ArenaManager {

    private final Map<String, Arena> arenasData = new HashMap<>();
    private final Map<String, MapData> mapsData = new HashMap<>();

    public void createArenasFromConfig(){

        File arenasConfigFile = new File(BedWars.getInstance().getDataFolder(), "default_arenas.yml");

        if(!arenasConfigFile.exists()){
            BedWars.getInstance().getLogger().log(
                    Level.WARNING,
                    "File with default arenas config does not exists, so no arenas will be created"
            );
            return;
        }

        FileConfiguration arenasConfig = YamlConfiguration.loadConfiguration(arenasConfigFile);

        ConfigurationSection arenasList = arenasConfig.getConfigurationSection("arenas");

        if (arenasList == null) {
            BedWars.getInstance().getLogger().log(
                    Level.WARNING,
                    "File with config of default arenas is empty, so no arenas will be created"
            );
            return;
        }
        for (String arenaId : arenasList.getKeys(false)){
            ConfigurationSection arenaConfig = arenasList.getConfigurationSection(arenaId);

            String mapId = arenaConfig.getString("mapId");

            MapData arenaMap = this.mapsData.get(mapId);

            if (arenaMap == null){
                BedWars.getInstance().getLogger().warning(
                    String.format("Invalid map id in arenas config in arena with id %s", arenaId)
                );
                continue;
            }

            Arena newArena = new Arena(arenaMap);

            this.arenasData.put(
                    arenaId,
                    newArena
            );

            newArena.initializeGame();

            BedWars.getInstance().getLogger().info(
                    String.format("Data of arena with id %s was successfully read from config", arenaId)
            );
        }
    }

    public void loadMapsData(){

        File mapsConfigFile = new File(BedWars.getInstance().getDataFolder(), "maps.yml");

        if(!mapsConfigFile.exists()){
            BedWars.getInstance().getLogger().log(
                    Level.WARNING,
                    "File with maps config does not exists, so no games will be created"
            );
            return;
        }

        FileConfiguration mapsConfig = YamlConfiguration.loadConfiguration(mapsConfigFile);

        ConfigurationSection mapsList = mapsConfig.getConfigurationSection("arenas");

        if (mapsList == null) {
            BedWars.getInstance().getLogger().log(
                    Level.WARNING,
                    "File with config of maps is empty, so no games will be created"
            );
            return;
        }
        for (String mapId : mapsList.getKeys(false)){
            ConfigurationSection mapConfig = mapsList.getConfigurationSection(mapId);


            ConfigurationSection teamsConfigurationSection = mapConfig.getConfigurationSection("teams");

            List<TeamConfig> teamConfigs = new ArrayList<>();


            // Reading teams config
            for (String teamId : teamsConfigurationSection.getKeys(false)){

                try{
                    // Check if correctly team color specified in config
                    TeamColor teamColor = TeamColor.valueOf(teamId.toUpperCase());

                    teamConfigs.add(
                            new TeamConfig(
                                    teamColor,
                                    Coordinates.fromConfig(teamsConfigurationSection.getConfigurationSection(teamId).getConfigurationSection("bed-location")),
                                    Coordinates.fromConfig(teamsConfigurationSection.getConfigurationSection(teamId).getConfigurationSection("spawn"))
                            )
                    );

                } catch (IllegalArgumentException e) {
                    BedWars.getInstance().getLogger().warning(
                            String.format("Invalid resource type in resource spawners section of maps config (mapId : %s)", mapId)
                    );
                }
            }

            ConfigurationSection resourceSpawnersConfig = mapConfig.getConfigurationSection("resource-spawners");

            Map<GameResource, List<Coordinates>> resourceSpawners = new HashMap<>();


            // Reading resource generators coordinates
            for (String resourceType : resourceSpawnersConfig.getKeys(false)){

                try{
                    // Check if correctly resource name specified in config
                    GameResource resource = GameResource.valueOf(resourceType.toUpperCase());

                    resourceSpawners.put(
                            resource,
                            new ArrayList<>()
                    );

                    List<Map<?, ?>> resourceSpawnPoints =  resourceSpawnersConfig.getMapList(resourceType);

                    for (Map<?, ?> coordinates : resourceSpawnPoints) {
                        int x = (int) coordinates.get("x");
                        int y = (int) coordinates.get("y");
                        int z = (int) coordinates.get("z");
                        resourceSpawners.get(resource).add(new Coordinates(x, y, z));
                    }

                } catch (IllegalArgumentException e) {
                    BedWars.getInstance().getLogger().warning(
                            String.format("Invalid resource type in resource spawners section of maps config (mapId : %s)", mapId)
                    );
                }

            }

            this.mapsData.put(
                    mapId,
                    new MapData(
                            mapId,
                            teamConfigs,
                            resourceSpawners
                    )
            );

            BedWars.getInstance().getLogger().info(
                    String.format("Data of map with id %s was successfully read from config", mapId)
            );
        }
    }

    public void forcedDestroyArenas(){
        for (String arenaId : this.arenasData.keySet()){
            Arena arena = this.arenasData.get(arenaId);

            arena.forcedDestroy();
        }
    }
}
