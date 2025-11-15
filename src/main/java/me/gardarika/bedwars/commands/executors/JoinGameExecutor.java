package me.gardarika.bedwars.commands.executors;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.ArenaManager;
import me.gardarika.bedwars.core.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGameExecutor extends BaseExecutor{
    private final ArenaManager arenaManager;
    private final PlayerManager playerManager;

    public JoinGameExecutor(){
        this.arenaManager = BedWars.getInstance().getArenaManager();
        this.playerManager = BedWars.getInstance().getPlayerManager();
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player p){
            Game playerCurrentGame = playerManager.getPlayerCurrentActiveGame(p);
            if (playerCurrentGame == null){
                if (args.length == 2) {
                    String arenaId = args[1];

                    Arena targetArena = arenaManager.getArena(arenaId);

                    if (targetArena == null) {
                        p.sendMessage("Invalid arena id");
                        return;
                    }

                    targetArena.playerJoin(p);
                }
            } else {
                p.sendMessage("You can't use this command here");
            }
        } else {
            sender.sendMessage("Only player can use this command");
        }
    }
}
