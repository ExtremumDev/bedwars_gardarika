package me.gardarika.bedwars.core.game.scheduler.tasks;

import me.gardarika.bedwars.core.game.players.GamePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class RevolvingTask extends BukkitRunnable {
    private int secondsLeft = 5;
    private final GamePlayer revolvingPlayer;

    public RevolvingTask(GamePlayer gamePlayer){
        this.revolvingPlayer = gamePlayer;
    }
    @Override
    public void run() {
        secondsLeft--;

        if (secondsLeft <= 0){
            this.revolvingPlayer.revolve();
        }
    }
}
