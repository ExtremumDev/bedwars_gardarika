package me.gardarika.bedwars.listeners.environemt;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
    private final PlayerManager playerManager;

    public BlockListener(){
        this.playerManager = BedWars.getInstance().getPlayerManager();
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e){
        Game playerGame = playerManager.getPlayerGame(e.getPlayer());

        if (playerGame == null){
            e.setCancelled(true);
        } else {
            e.setCancelled(playerGame.handleBlockDestroy(e.getBlock(), e.getPlayer()));
        }
    }
}
