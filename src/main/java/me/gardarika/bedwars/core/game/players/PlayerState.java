package me.gardarika.bedwars.core.game.players;

public enum PlayerState {
    ALIVE, // In game, alive player
    DEAD, // In game, dead but in  reviving
    LOST, // In game, dead and lost, spectating or left
    SPECTATOR; // Not game player at start, spectating, join after game started
}
