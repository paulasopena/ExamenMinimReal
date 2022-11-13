package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Activity;
import edu.upc.dsa.models.Departure;
import edu.upc.dsa.models.Game;

import java.text.SimpleDateFormat;
import java.util.*;

import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);
    Map<String, User> users;
    Map<String,Game> games;
    Game gameUnmodified;

    public GameManagerImpl() {
        this.users= new HashMap<>();
        this.games=new HashMap<String,Game>();
        this.gameUnmodified=new Game();

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
    public void StartGame(String idUser, String idGame1) throws MoreThanOneActiveGame, UserDoesNotExistException, GameDoesNotExistException{
        logger.info("Starting a new Game...");
        ControlOfExistenceOfUser(idUser);
        ControlOfExistenceOfGame(idGame1);
        
        Game game=this.games.get(idGame1);

        //this.gameUnmodified=new Game(game.getName(),game.getDescription(),game.getNumberOfLevels());
        this.users.get(idUser).addGameToList(game);
        Map<String,Game> gameAndDepartures;
        gameAndDepartures = this.users.get(idUser).getGames();
        List<Departure> departures;
        departures=gameAndDepartures.get(idGame1).getDepartures();
        ControlOfMoreThanOneActiveGame(idUser);
        Departure newDeparture=new Departure(idGame1,idUser);
        departures.add(newDeparture);
        this.games.get(idGame1).AddGamerToList(this.users.get(idUser));
        logger.info("Game correctly started for user ID "+ idUser+ " and game play ID " +newDeparture.getIdDeparture()+ " and game ID "+idGame1);
    }

    @Override
    public Map<String, User> getUsers() {
        return this.users;
    }

    @Override
    public int ActualLevel(String idUser) throws UserDoesNotExistException,NoActiveGameException{
        ControlOfExistenceOfUser(idUser);
        logger.info("Starting looking for the actual level of the user...");
        Departure departureActive= SearchForActiveDeparture(idUser);
        if(departureActive==null){
            throw new NoActiveGameException();
        }
        logger.info("The level of the user is..."+departureActive.getLevel());
        return departureActive.getLevel();
    }

    @Override
    public int ActualPoints(String idUser) throws UserDoesNotExistException, NoActiveGameException {
        ControlOfExistenceOfUser(idUser);
        logger.info("Starting looking for the actual points of the user...");
        Departure departureActive= SearchForActiveDeparture(idUser);
        ControlOfNoActiveGame(idUser);
        logger.info("The points of the user are..."+departureActive.getPointsOfTheDeparture());
        return departureActive.getPointsOfTheDeparture();
    }

    @Override
    public Activity ChangeOfLevel(String idUser,String idGame) throws UserDoesNotExistException, NoActiveGameException {
        ControlOfExistenceOfUser(idUser);
        logger.info("Changing level of the user...");
        Departure departureActive= SearchForActiveDeparture(idUser);
        ControlOfNoActiveGame(idUser);
        departureActive.setPointsOfTheDeparture(this.users.get(idUser).getGames().get(idGame).getListOfLevelsOfGame().get(departureActive.getLevel()+1).getPoints());
        departureActive.setLevel(departureActive.getLevel()+1);
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Activity newActivity =new Activity(idUser,departureActive.getLevel(),date.getTime(),departureActive.getActive(),departureActive.getPointsOfTheDeparture());
        this.users.get(idUser).getGames().get(idGame).getActivityOfTheGame().add(newActivity);
        logger.info("The user id is "+idUser+" the date of the change of level is "+date.getTime()+"and the actual level is "+departureActive.getLevel());
        logger.info("The number of points "+ this.users.get(idUser).getName()+" has is "+departureActive.getPointsOfTheDeparture());
        return newActivity;
    }

    @Override
    public Departure SearchForActiveDeparture(String idUser){
        logger.info("Looking for active departures");
        List<Game> listOfGames = new ArrayList<>(this.users.get(idUser).getGames().values());
        for(int j=0; j<listOfGames.size();j++){
            List<Departure> departures= listOfGames.get(j).getDepartures();
            logger.info("Checking if the user has more than one active departure in"+ listOfGames.get(j).getName());
            for(int i = 0; i< departures.size(); i++){
                if(departures.get(i).getActive()==1){
                    logger.info("User has one active departure");
                    return departures.get(i);
                }
            }
        }
        return null;
    }
    @Override
    public void EndingGame(String idUser, String idGame) throws UserDoesNotExistException, NoActiveGameException {
        logger.info("Ending Game starting...");
        ControlOfExistenceOfUser(idUser);
        Departure departureActive=SearchForActiveDeparture(idUser);
        ControlOfNoActiveGame(idUser);
        departureActive.setActive(0);
        Date date=new Date();
        Activity newActivity =new Activity(idUser,departureActive.getLevel(),date.getTime(),departureActive.getActive(),departureActive.getPointsOfTheDeparture());
        this.users.get(idUser).getGames().get(idGame).getActivityOfTheGame().add(newActivity);
        logger.info("The user "+ idUser+" has ended the game!");

    }
    @Override
    public List<User> usersByPoints(String idGame) throws GameDoesNotExistException {
        logger.info("Returning users sorted by points...");
        ControlOfExistenceOfGame(idGame);
        List<User> usersOfTheGame= this.games.get(idGame).getGamers();
        usersOfTheGame.sort((u1,u2)->Integer.compare(u2.getGames().get(idGame).getLastDeparture().getPointsOfTheDeparture(),u1.getGames().get(idGame).getLastDeparture().getPointsOfTheDeparture()));
        return usersOfTheGame;
    }
    @Override
    public List<Activity> getActivityOfUser(String idUser, String idGame) throws UserDoesNotExistException {
        logger.info("Returning Activity of the user...");
        ControlOfExistenceOfUser(idUser);
        return this.users.get(idUser).getGames().get(idGame).getActivityOfTheGame();
    }
    @Override
    public List<Departure> getGames(String idUser) throws UserDoesNotExistException {
        logger.info("Returning Activity of the user...");
        ControlOfExistenceOfUser(idUser);
        List<Game> gamesOfUser= new ArrayList<>(this.users.get(idUser).getGames().values());
        List<Departure> totalDepartures= new ArrayList<>();
        for(int i=0;i<gamesOfUser.size();i++){
            totalDepartures.addAll(gamesOfUser.get(i).getDepartures());
        }
        logger.info("The total games of" +this.users.get(idUser).getName()+ " are "+ totalDepartures.size());
        return totalDepartures;
    }

    @Override
    public void ControlOfExistenceOfUser(String idUser) throws UserDoesNotExistException {
        if(this.users.get(idUser)==null){
            logger.info("User does not exist!");
            throw new UserDoesNotExistException();
        }

    }

    @Override
    public void ControlOfExistenceOfGame(String idGame) throws GameDoesNotExistException {
        if(this.games.get(idGame)==null){
            logger.info("Game does not exist.");
            throw new GameDoesNotExistException();
        }

    }
    @Override
    public void ControlOfMoreThanOneActiveGame(String idUser) throws MoreThanOneActiveGame {
        if(SearchForActiveDeparture(idUser)!=null){
            throw new MoreThanOneActiveGame();
        }
    }
    @Override
    public void ControlOfNoActiveGame(String idUser) throws NoActiveGameException {
        if(SearchForActiveDeparture(idUser)==null){
            throw new NoActiveGameException();
        }
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