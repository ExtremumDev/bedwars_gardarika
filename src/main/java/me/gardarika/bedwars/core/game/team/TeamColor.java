package me.gardarika.bedwars.core.game.team;

import org.bukkit.ChatColor;

import java.awt.*;

public enum TeamColor {
    RED("Красная Дружина", "§c", ChatColor.RED, org.bukkit.Color.RED),
    BLUE("Синяя Рать", "§9", ChatColor.BLUE, org.bukkit.Color.BLUE),
    GREEN("Зеленая Варта", "§2", ChatColor.DARK_GREEN, org.bukkit.Color.GREEN),
    YELLOW("Желтый Стяг", "§e", ChatColor.YELLOW, org.bukkit.Color.YELLOW),
    AQUA("Лазурная Дружина", "§b", ChatColor.AQUA, org.bukkit.Color.AQUA),
    WHITE("Белая Гвардия", "§f", ChatColor.WHITE, org.bukkit.Color.WHITE),
    PINK("Розовый Отряд", "§d", ChatColor.LIGHT_PURPLE, org.bukkit.Color.FUCHSIA),
    GRAY("Серая Стая", "§7", ChatColor.GRAY, org.bukkit.Color.GRAY);

    private final String displayName;  // Красивое название
    private final String colorCode;    // Для чата §c
    private final ChatColor chatColor; // Bukkit ChatColor
    private final org.bukkit.Color bukkitColor; // Для окрашивания

    TeamColor(String displayName, String colorCode, ChatColor chatColor, org.bukkit.Color bukkitColor) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.chatColor = chatColor;
        this.bukkitColor = bukkitColor;
    }
}
