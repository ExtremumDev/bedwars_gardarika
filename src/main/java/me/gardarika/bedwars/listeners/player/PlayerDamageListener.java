package me.gardarika.bedwars.listeners.player;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.BanEntry;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final PlayerManager playerManager;

    public PlayerDamageListener(){
        this.playerManager = BedWars.getInstance().getPlayerManager();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player p){
            Game game = playerManager.getPlayerGame(p);

            if (game == null){

            } else{
                game
            }
        }
    }

}
