package me.gardarika.bedwars.core.managers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LobbyManager {
    private final Location lobbySpawn;

    public LobbyManager(){
        this.lobbySpawn = new Location(Bukkit.getWorld("world"), 8, -60, 8);
    }

    public void movePlayerToLobby(Player p){
        setLobbySettings(p);
        teleportPlayerToLobbySpawn(p);
        setInventoryItems(p);
    }

    public void teleportPlayerToLobbySpawn(Player p){
        p.teleport(lobbySpawn);
    }

    public void setLobbySettings(Player p){
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.setFireTicks(0);
    }

    public void setInventoryItems(Player p){
        Inventory playerInventory = p.getInventory();

        playerInventory.setItem(8, new ItemStack(Material.RED_BED));
    }
}
