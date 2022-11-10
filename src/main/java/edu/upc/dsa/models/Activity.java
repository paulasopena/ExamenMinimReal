package edu.upc.dsa.models;

import java.util.Date;

public class Activity {
    String level;
    long date;
    int state;
    int points;
    public Activity(){};
    public Activity(String level, long date, int state, int points){
        this.level=level;
        this.date=date;
        this.state=state;
        this.points=points;
    }
}
