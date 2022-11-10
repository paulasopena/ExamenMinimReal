package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.Track;
import edu.upc.dsa.models.User;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameManager {

    /*
    public Track addTrack(String title, String singer);
    public Track addTrack(Track t);
    public Track getTrack(String id);
    public List<Track> findAll();
    public void deleteTrack(String id);
    public Track updateTrack(Track t);
    */
    public int size();


    String addUser(String name, String surname, String email, String password) throws EmailAlreadyBeingUsedException;

    String addGame(String name, String description, int numberOfLevels);

    void StartGame(String idUser, String idGame1) throws MoreThanOneActiveGame, UserDoesNotExistException, GameDoesNotExistException;

    Map<String, User> getUsers();

    int ActualLevel(String idUser) throws UserDoesNotExistException, NoActiveGameException;

    int ActualPoints(String idUser) throws UserDoesNotExistException,NoActiveGameException;
    int ChangeOfLevel(String idUser) throws UserDoesNotExistException, NoActiveGameException;

    List<Game> getGamesOfUser(String idAlba) throws UserDoesNotExistException;
}
