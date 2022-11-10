package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    String id;
    String name;
    String description;
    int totalPoints;
    int active;
    int level;
    List<Activity> activityOfTheGame;
    List<Levels> levels;
    public Game(){};
    public Game(String name, String description,int numberOfLevels){
        this.name=name;
        this.description=description;
        this.id = RandomUtils.getId();
        this.totalPoints =50;
        this.active=0;
        this.level=0;
        this.activityOfTheGame=new ArrayList<>();
        this.levels=new ArrayList<>();
        this.setLevels(numberOfLevels);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<Activity> getActivityOfTheGame() {
        return activityOfTheGame;
    }

    public void setActivityOfTheGame(List<Activity> activityOfTheGame) {
        this.activityOfTheGame = activityOfTheGame;
    }
    public void setLevels(int numberOfLevels){
        for(int i =0; i<numberOfLevels;i++){
            String name="Level"+i;
            Levels l= new Levels(this.id,name);
            if(i==0){
                l.setPoints(0);
            }
            else if(i==numberOfLevels-1){
                l.setPoints(this.levels.get(i-1).getPoints()+100);
            }
            else{
                l.setPoints(this.levels.get(i-1).getPoints()+50);
            }
            this.levels.add(l);
        }

    }
    public int getLevel(){
        return(this.level);
    }
    public void setLevel(int levelActual){
        this.level=levelActual;
    }
    public List<Levels> getListOfLevelsOfGame(){
        return this.levels;
    }
}
