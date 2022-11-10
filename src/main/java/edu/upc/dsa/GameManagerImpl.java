package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Activity;
import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.Track;

import java.util.*;

import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);
    Map<String, User> users;
    Map<String, Game> games;

    public GameManagerImpl() {
        this.users= new HashMap<>();
        this.games=new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance==null) instance = new GameManagerImpl();
        return instance;
    }
    public int size() {
        int ret = this.games.size();
        logger.info("size " + ret);

        return ret;
    }
    @Override
    public String addUser(String name, String surname, String email, String password) throws EmailAlreadyBeingUsedException{
        logger.info("Adding a new User Starting...");
        User newUser= new User(name,surname,email,password);
        List<User> userList = new ArrayList<>(this.users.values());
        logger.info("Checking whether this users exists...");

        for(User u : userList){
            if(Objects.equals(u.getMail(), email)){
                logger.info("The user already exists!");
                throw new EmailAlreadyBeingUsedException();
            }
        }
        this.users.put(newUser.id, newUser);
        logger.info("User " + newUser.getName() +" has been added correctly with the id " + newUser.getId());
        return (newUser.id);
    }

    @Override
    public String addGame(String name, String description, int numberOfLevels) {
        logger.info("Creation of a new game starting...");
        Game game= new Game(name,description,numberOfLevels);
        this.games.put(game.getId(),game);
        logger.info("Game successfully created with id "+game.getId());
        return game.getId();
    }

    @Override
    public void StartGame(String idUser, String idGame1) throws MoreThanOneActiveGame, UserDoesNotExistException, GameDoesNotExistException {
        logger.info("User starting a new game...");
        Game game=this.games.get(idGame1);
        if(game==null){
            logger.info("Game does not exist.");
            throw new GameDoesNotExistException();
        }
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }
        this.users.get(idUser).addGameToList(game);
        List<Game> partidas=this.users.get(idUser).getGames();
        for(int i=0; i<partidas.size();i++){
            if(partidas.get(i).getActive()==1){
                logger.info("User has more than one active game");
                throw new MoreThanOneActiveGame();
            }
        }
        Game gameUser= this.users.get(idUser).getGameWithID(idGame1);
        gameUser.setActive(1);
        Date date= new Date();
        Activity newActivity=new Activity(idGame1,date.getTime(),gameUser.getActive(),gameUser.getTotalPoints());
        logger.info("Game correctly started.");
    }

    @Override
    public Map<String, User> getUsers() {
        return this.users;
    }

    @Override
    public int ActualLevel(String idUser) throws UserDoesNotExistException,NoActiveGameException{
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }
        List<Game> partidas=this.users.get(idUser).getGames();
        for(int i=0; i<partidas.size();i++){
            if(partidas.get(i).getActive()==1)
                return partidas.get(i).getLevel();
        }
        throw new NoActiveGameException();
    }

    @Override
    public int ActualPoints(String idUser) throws UserDoesNotExistException, NoActiveGameException {
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }
        logger.info("Starting giving actual points");
        List<Game> partidas=this.users.get(idUser).getGames();
        for(int i=0; i<partidas.size();i++){
            if(partidas.get(i).getActive()==1)
                logger.info("Actual points"+partidas.get(i).getTotalPoints());
                return partidas.get(i).getTotalPoints();
        }
        throw new NoActiveGameException();
    }

    @Override
    public int ChangeOfLevel(String idUser) throws UserDoesNotExistException, NoActiveGameException {
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }
        logger.info("Starting change of level");
        List<Game> partidas=this.users.get(idUser).getGames();
        for(int i=0; i<partidas.size();i++){
            int actualLevel= partidas.get(i).getLevel();
            if(partidas.get(i).getActive()==1){
                actualLevel=actualLevel+1;
                partidas.get(i).setLevel(actualLevel);
                partidas.get(i).setTotalPoints(partidas.get(i).getTotalPoints()+partidas.get(i).getListOfLevelsOfGame().get(actualLevel).getPoints());
                logger.info("Change done successfully. Actual level"+partidas.get(i).getLevel());
                return partidas.get(i).getLevel();
            }
        }
        throw new NoActiveGameException();

    }

    @Override
    public List<Game> getGamesOfUser(String idUser) throws UserDoesNotExistException {
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }
        return this.users.get(idUser).getGames();
    }




    /*
    public Track addTrack(Track t) {
        logger.info("new Track " + t);

        this.tracks.add (t);
        logger.info("new Track added");
        return t;
    }

    public Track addTrack(String title, String singer) {
        return this.addTrack(new Track(title, singer));
    }

    public Track getTrack(String id) {
        logger.info("getTrack("+id+")");

        for (Track t: this.tracks) {
            if (t.getId().equals(id)) {
                logger.info("getTrack("+id+"): "+t);

                return t;
            }
        }

        logger.warn("not found " + id);
        return null;
    }

    public List<Track> findAll() {
        return this.tracks;
    }

    @Override
    public void deleteTrack(String id) {

        Track t = this.getTrack(id);
        if (t==null) {
            logger.warn("not found " + t);
        }
        else logger.info(t+" deleted ");

        this.tracks.remove(t);

    }

    @Override
    public Track updateTrack(Track p) {
        Track t = this.getTrack(p.getId());

        if (t!=null) {
            logger.info(p+" rebut!!!! ");

            t.setSinger(p.getSinger());
            t.setTitle(p.getTitle());

            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+p);
        }

        return t;
    }
    */

}