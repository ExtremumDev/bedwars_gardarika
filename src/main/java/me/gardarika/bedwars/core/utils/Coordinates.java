package me.gardarika.bedwars.core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Coordinates {
    public int x, y, z;

    public Coordinates(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coordinates fromConfig(ConfigurationSection section) {
        if (section == null) {
            throw new IllegalArgumentException("ConfigurationSection is null!");
        }

        return new Coordinates(
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z")
        );
    }

    public static List<Coordinates> listFromConfig(List<Map<?, ?>> list) {
        List<Coordinates> coordsList = new ArrayList<>();
        if (list == null) return coordsList;

        for (Map<?, ?> map : list) {
            Object xConfig = map.get("x");
            Object yConfig = map.get("y");
            Object zConfig = map.get("z");

            if (xConfig instanceof Number && yConfig instanceof Number && zConfig instanceof Number) {
                coordsList.add(new Coordinates(
                        ((Number) xConfig).intValue(),
                        ((Number) yConfig).intValue(),
                        ((Number) zConfig).intValue()
                ));
            }
        }

        return coordsList;
    }

    public static List<Coordinates> listFromConfig(FileConfiguration config, String path) {
        return listFromConfig(config.getMapList(path));
    }

    public Location toLocation(World world){
        return new Location(world, x, y, z);
    }
}
