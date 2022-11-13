package edu.upc.dsa.models;

import java.util.Date;

public class Activity {
    String idUser;
    int level;
    long date;
    int state;
    int points;
    public Activity(){};
    public Activity(String idUser, int level, long date, int state, int points){
        this.idUser=idUser;
        this.level=level;
        this.date=date;
        this.state=state;
        this.points=points;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
