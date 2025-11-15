package me.gardarika.bedwars.commands.executors;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWLeaveExecutor extends BaseExecutor{
    private final PlayerManager playerManager;

    public BWLeaveExecutor(){
        this.playerManager = BedWars.getInstance().getPlayerManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player p){
            Game playerCurrentGame = playerManager.getPlayerCurrentActiveGame(p);

            if (playerCurrentGame == null){
                // Leave bed wars server to hub
            } else {
                playerCurrentGame.playerLeave(p);
            }
        }else {
            sender.sendMessage("Only players can use this command");
        }
    }
}
