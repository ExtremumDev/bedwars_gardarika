package me.gardarika.bedwars.core.game.scheduler.tasks;

import me.gardarika.bedwars.core.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {
    private int count = 60;
    private final Game game;

    public CountdownTask(Game game){
        this.game = game;
    }
    @Override
    public void run() {
        count--;

        if (count == 0){
            game.startGame();
        }
    }
}
