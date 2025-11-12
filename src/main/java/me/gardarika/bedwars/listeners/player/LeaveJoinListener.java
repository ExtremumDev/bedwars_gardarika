package me.gardarika.bedwars.listeners.player;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.LobbyManager;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveJoinListener implements Listener {
    private final PlayerManager playerManager;
    private final LobbyManager lobbyManager;

    public LeaveJoinListener(){
        this.playerManager = BedWars.getInstance().getPlayerManager();
        this.lobbyManager = BedWars.getInstance().getLobbyManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){


        // Data base check, data get

        lobbyManager.teleportPlayerToLobbySpawn(e.getPlayer());

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        // Get player's game

        Game currentPlayerGame = playerManager.getPlayerGame(e.getPlayer());

        if (currentPlayerGame != null){
            currentPlayerGame.playerLeave(e.getPlayer());
        }
    }
}
