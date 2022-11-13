package edu.upc.dsa.TransferObjects;

public class GameInformation {
    String name;
    String description;
    int numberOfLevels;
    public GameInformation(){};

    public GameInformation(String name, String description, int numberOfLevels){
        this.name=name;
        this.description=description;
        this.numberOfLevels=numberOfLevels;
        this.setName(name);
        this.setDescription(description);
        this.setNumberOfLevels(numberOfLevels);
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

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }
}
