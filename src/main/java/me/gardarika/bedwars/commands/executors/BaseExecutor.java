package me.gardarika.bedwars.commands.executors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseExecutor {

    public abstract void execute(CommandSender sender, String[] args);
}
