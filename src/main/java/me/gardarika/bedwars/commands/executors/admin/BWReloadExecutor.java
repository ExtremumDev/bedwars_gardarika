package me.gardarika.bedwars.commands.executors.admin;

import me.gardarika.bedwars.BedWars;
import me.gardarika.bedwars.commands.executors.BaseExecutor;
import me.gardarika.bedwars.core.managers.ArenaManager;
import org.bukkit.command.CommandSender;

public class BWReloadExecutor extends BaseExecutor {
    private final ArenaManager arenaManager;

    public BWReloadExecutor(){
        this.arenaManager = BedWars.getInstance().getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        arenaManager.turnOffAllArenas();
        arenaManager.createArenasFromConfig();
    }
}
