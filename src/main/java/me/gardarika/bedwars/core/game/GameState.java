package me.gardarika.bedwars.core.game;

public enum GameState {
    WAITING,      // Ожидание игроков
    STARTING,     // Отсчёт до начала
    ACTIVE,       // Идёт активная игра
    FINISHED,     // Игра завершена, показаны результаты, идёт празднование
}
