package me.gardarika.bedwars.core.game.players;

import me.gardarika.bedwars.core.game.team.Team;

public class PlayerGameStatistic {
    private int kills = 0;
    private int finalKills = 0;
    private int deaths = 0;
    private int bedDestroyed = 0;
    private boolean isWinner = false;


    public void addDestroyedBed(){
        this.bedDestroyed++;
    }

    public void addKill(boolean isFinal){
        this.kills++;

        if (isFinal){
            this.finalKills++;
        }
    }

    public void addDeath(){
        this.deaths++;
    }

    public void win(){
        this.isWinner = true;
    }

    public int getKills() {
        return kills;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBedDestroyed() {
        return bedDestroyed;
    }

    public boolean isWinner() {
        return isWinner;
    }
}
