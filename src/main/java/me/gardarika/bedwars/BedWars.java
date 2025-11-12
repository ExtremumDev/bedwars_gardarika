package me.gardarika.bedwars;

import me.gardarika.bedwars.core.managers.ArenaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWars extends JavaPlugin {

    private static BedWars instance;

    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        instance = this;

        // creating managers

        this.arenaManager = new ArenaManager();

        arenaManager.loadMapsData();

        // start game set up

        arenaManager.createArenasFromConfig();
    }

    @Override
    public void onDisable() {
        this.arenaManager.forcedDestroyArenas();
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static BedWars getInstance() {
        return instance;
    }
}
