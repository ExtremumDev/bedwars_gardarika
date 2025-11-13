package me.gardarika.bedwars.core.arena;

import me.gardarika.bedwars.core.config.MapData;
import me.gardarika.bedwars.core.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

public class Arena {
    /**
     * Arena represents minecraft world or space in minecraft where games are playing
     * In one map(in game arena) can be played a lot of games. So game is permanent property of arena that describes only current game on arena
     */

    private ArenaState state = ArenaState.CLEARING;
    private final MapData map;
    private Game currentGame;

    private WeakReference<World> gameWorld;

    public Arena(MapData mapData){
        this.map = mapData;
        this.currentGame = null;
    }

    public void initializeGame(){
        if (currentGame == null){
            return;
        }

        this.gameWorld = new WeakReference<>(loadGameWorld());

        this.currentGame = new Game(this);
    }

    public void playerJoin(Player player){
        switch (this.state){
            case DISABLED:
                player.sendMessage(ChatColor.RED + "Arena is currently disabled, try later");
                break;
            case CLEARING:
                player.sendMessage(ChatColor.RED + "Arena is clearing now, try later");
                break;
            case READY:
                if (currentGame == null){
                    player.sendMessage(ChatColor.RED + "Failed to join this game, try later");
                    return;
                }

                currentGame.addPlayer(player);
        }
    }

    private World loadGameWorld(){
        return Bukkit.createWorld(new WorldCreator("game"));
    }

    public void forcedDestroy(){
        if (state.equals(ArenaState.READY)){
            this.forcedEndGame();
        }
    }

    public void forcedEndGame(){
        if (currentGame != null){
            this.currentGame.endForced();
        }
    }

    public void clearArena(){

    }

    public MapData getMap() {
        return map;
    }

    public World getGameWorld() {
        return gameWorld.get();
    }
}
