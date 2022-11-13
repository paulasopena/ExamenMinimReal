package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.*;

public class Game {
    String id;
    String name;
    String description;
    int numberOfLevels;
    List<Activity> activityOfTheGame;
    List<Levels> levels;
    List<Departure> departures;
    List<User> gamers;
    public Game(){};
    public Game(String name, String description,int numberOfLevels){
        this.name=name;
        this.description=description;
        this.id = RandomUtils.getId();
        this.activityOfTheGame=new ArrayList<>();
        this.levels=new ArrayList<>();
        this.numberOfLevels=numberOfLevels;
        this.departures=new ArrayList<>();
        this.gamers=new ArrayList<>();
        this.setLevels(numberOfLevels);
    }
    public List<Departure> getDepartures(){
        return this.departures;
    }
    public String getId() {
        return id;
    }
    public int getNumberOfLevels(){
        return numberOfLevels;
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

    public List<Levels> getListOfLevelsOfGame(){
        return this.levels;
    }
    public Departure getDepartureById(String id){
        for (Departure departure : this.departures) {
            if (Objects.equals(departure.getIdDeparture(), id)) {
                return departure;
            }
        }
        return null;
    }
    public List<User> getGamers(){
        return this.gamers;
    }
    public void AddGamerToList(User user){
        this.gamers.add(user);
    }
    public Departure getLastDeparture(){
        return this.departures.get(this.departures.size()-1);
    }
}
