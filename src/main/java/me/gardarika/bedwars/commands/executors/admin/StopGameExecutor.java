package me.gardarika.bedwars.commands.executors.admin;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.commands.executors.BaseExecutor;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.ArenaManager;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameExecutor extends BaseExecutor {
    private final PlayerManager playerManager;

    public StopGameExecutor(){
        this.playerManager = BedWars.getInstance().getPlayerManager();
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player p){
            Game currentGame = playerManager.getPlayerCurrentActiveGame(p);

            if (currentGame != null){
                currentGame.getArena().forcedEndGame();
            } else {
                p.sendMessage("You are not on any arena");
            }
        } else {
            sender.sendMessage("Only players allowed to use this command");
            return;
        }
    }
}
