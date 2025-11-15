package me.gardarika.bedwars.commands;

import me.gardarika.bedwars.commands.executors.BaseExecutor;
import me.gardarika.bedwars.commands.executors.BWLeaveExecutor;
import me.gardarika.bedwars.commands.executors.admin.CreateArenaExecutor;
import me.gardarika.bedwars.commands.executors.JoinGameExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainBedWarsCommand implements CommandExecutor {

    private final Map<String, BaseExecutor> bwCommandsExecutors = new HashMap<>();

    public MainBedWarsCommand(){
        bwCommandsExecutors.put(
                "create-arena",
                new CreateArenaExecutor()
        );

        bwCommandsExecutors.put(
                "join",
                new JoinGameExecutor()
        );
        bwCommandsExecutors.put(
                "leave",
                new BWLeaveExecutor()
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0){
            sendHelpInformation();
        }

        String option = args[0];

        BaseExecutor commandExecutor = bwCommandsExecutors.get(option);

        if (commandExecutor == null){
            commandSender.sendMessage("Invalid option");
            return true;
        }

        commandExecutor.execute(commandSender, args);
        return true;
    }

    private void sendHelpInformation(){

    }
}
