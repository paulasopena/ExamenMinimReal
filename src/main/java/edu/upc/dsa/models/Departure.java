package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import io.swagger.models.auth.In;

import java.util.List;

public class Departure {
    String idDeparture;
    String idUser;
    String idGame;
    Integer pointsOfTheDeparture;
    int active;
    int level;
    public Departure(){};
    public Departure(String idGame,String idUser) {
        this.idGame = idGame;
        this.idUser=idUser;
        this.pointsOfTheDeparture = 50;
        this.active = 1;
        this.level = 0;
        this.idDeparture=RandomUtils.getId();
    }

    public String getIdDeparture() {
        return idDeparture;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public Integer getPointsOfTheDeparture() {
        return pointsOfTheDeparture;
    }

    public void setPointsOfTheDeparture(int addedPoints) {
        this.pointsOfTheDeparture = this.pointsOfTheDeparture+addedPoints;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
