package me.gardarika.bedwars.commands.executors.admin;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.commands.executors.BaseExecutor;
import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.managers.ArenaManager;
import org.bukkit.command.CommandSender;

public class CreateArenaExecutor extends BaseExecutor {
    private final ArenaManager arenaManager;

    public CreateArenaExecutor(){
        this.arenaManager = BedWars.getInstance().getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 4){
            String arenaId = args[1];
            String mapId = args[2];
            String stringTotalPlayers = args[3];

            MapData mapData = arenaManager.getMapData(mapId);

            if (mapData == null){
                sender.sendMessage("Invalid map id");
                return;
            }

            int totalPlayers;
            try {
                totalPlayers = Integer.parseInt(stringTotalPlayers);

                arenaManager.createArena(mapData, arenaId, totalPlayers);
            } catch (NullPointerException e){
                sender.sendMessage("Invalid players number, type number!!!!");
            }

        } else {
            sender.sendMessage("Usage: /bw <arena-id> <map-id> <players-number>");
        }
    }
}
