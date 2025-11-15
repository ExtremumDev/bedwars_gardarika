package me.gardarika.bedwars;

import me.gardarika.bedwars.commands.MainBedWarsCommand;
import me.gardarika.bedwars.core.managers.ArenaManager;
import me.gardarika.bedwars.core.managers.LobbyManager;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWars extends JavaPlugin {

    private static BedWars instance;

    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        instance = this;

        // creating managers

        this.arenaManager = new ArenaManager();

        arenaManager.loadMapsData();

        // start game set up

//        arenaManager.createArenasFromConfig();

        playerManager = new PlayerManager();

        lobbyManager = new LobbyManager();

        PluginCommand mainBWCommand = getCommand("bedwars");

        if (mainBWCommand != null){
            mainBWCommand.setExecutor(
                    new MainBedWarsCommand()
            );
        } else {
            getLogger().warning("[Invalid config] \"/bedwars\" command not found in plugin yml, some features will not work");
        }
    }

    @Override
    public void onDisable() {
        this.arenaManager.turnOffAllArenas();
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static BedWars getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}
