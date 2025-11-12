package me.gardarika.bedwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainBedWarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0){
            sendHelpInformation();
        }

        String option = args[0];

        switch (option){
            case "leave":

            case "reload":

        }
        return false;
    }

    private void sendHelpInformation(){

    }
}
