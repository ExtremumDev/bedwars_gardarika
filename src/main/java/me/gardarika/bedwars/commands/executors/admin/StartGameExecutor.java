package me.gardarika.bedwars.commands.executors.admin;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.commands.executors.BaseExecutor;
import me.gardarika.bedwars.core.arena.Arena;
import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.game.Game;
import me.gardarika.bedwars.core.managers.ArenaManager;
import org.bukkit.command.CommandSender;

public class StartGameExecutor extends BaseExecutor {
    private final ArenaManager arenaManager;

    public StartGameExecutor(){
        this.arenaManager = BedWars.getInstance().getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2){
            String arenaId = args[1];

            Arena arena = arenaManager.getArena(arenaId);

            if (arena == null){
                sender.sendMessage("Invalid arena id");
                return;
            }

            arena.startGame();

        } else {
            sender.sendMessage("Usage: /bw start <arena-id>");
        }
    }
}
