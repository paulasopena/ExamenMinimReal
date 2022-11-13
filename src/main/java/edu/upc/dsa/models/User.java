package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.*;

public class User {
    public String id;
    String name;
    String surname;
    String mail;
    String password;
    Map<String,Game> gamesAndDepartures;

    public User(){};
    public User(String name, String surname, String mail, String password){
        this.name=name;
        this.surname=surname;
        this.mail=mail;
        this.password=password;
        this.id = RandomUtils.getId();
        this.gamesAndDepartures =new HashMap<String,Game>();
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String,Game> getGames() {
        return this.gamesAndDepartures;
    }

    public void addGameToList(Game game) {
        this.gamesAndDepartures.put(game.getId(),game);
    }
    public Game getGameWithID(String idGame){
        for(int i = 0; i<this.gamesAndDepartures.size(); i++){
            if(Objects.equals(this.gamesAndDepartures.get(i).getId(), idGame))
                return this.gamesAndDepartures.get(i);
        }
        return null;
    }
}
