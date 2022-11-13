package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Activity;
import edu.upc.dsa.models.Departure;
import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.User;
import org.glassfish.jersey.server.model.ModelValidationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class GameManagerImplTest {
    GameManager gm;
    String idAlba;
    String idPaula;
    String idAlberto;
    String idGame1;

    @Before
    public void setUp() throws EmailAlreadyBeingUsedException {
        gm = new GameManagerImpl();
        this.idAlba=gm.addUser("Alba","Roma","albaroma@gmail.com","123456");
        this.idPaula=gm.addUser( "Paula","Sopena","paulasopena@gmail.com","123456");
        this.idAlberto=gm.addUser("Alberto", "Cruz","albertocruz@gmail.com","123456");
        this.idGame1 = gm.addGame("Chess","Have fun!",5);

    }
    @After
    public void tearDown(){this.gm=null;}
    @Test
    public void UserStartingAGame() throws UserDoesNotExistException, GameDoesNotExistException, MoreThanOneActiveGame{
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.StartGame("JEJEJ",idGame1));
        Assert.assertThrows(GameDoesNotExistException.class, ()->this.gm.StartGame(idAlba,"Juanete"));
        this.gm.StartGame(idAlba,idGame1);
        int active=this.gm.getUsers().get(idAlba).getGames().get(idGame1).getDepartures().get(0).getActive();
        Assert.assertEquals(1,active);
    }


    @Test
    public void ActualLevel() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException, CloneNotSupportedException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        Assert.assertThrows(NoActiveGameException.class, ()->this.gm.ActualLevel(idAlba));
        UserStartingAGame();
        Assert.assertEquals(0,this.gm.ActualLevel(idAlba));
    }

    @Test
    public void ActualPoints() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException, CloneNotSupportedException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        Assert.assertThrows(NoActiveGameException.class, ()->this.gm.ActualLevel(idAlba));
        UserStartingAGame();
        Assert.assertEquals(50,this.gm.ActualPoints(idAlba));
    }

    @Test
    public void ChangeOfLevel() throws UserDoesNotExistException, NoActiveGameException, GameDoesNotExistException, MoreThanOneActiveGame, CloneNotSupportedException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        UserStartingAGame();
        Activity activity=this.gm.ChangeOfLevel(idAlba,idGame1);
        Assert.assertEquals(1,activity.getLevel());

    }

    @Test
    public void GamesOfUser() throws UserDoesNotExistException, MoreThanOneActiveGame, GameDoesNotExistException {
        UserStartingAGame();
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        List<Departure> listGames=this.gm.getGames(idAlba);
        Assert.assertEquals(1,listGames.size());
    }



    @Test
    public void EndingGame() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException, CloneNotSupportedException {
        UserStartingAGame();
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.EndingGame("JEJEJ",idGame1));
        this.gm.EndingGame(idAlba,idGame1);
        int active=this.gm.getUsers().get(idAlba).getGames().get(idGame1).getDepartures().get(0).getActive();
        Assert.assertEquals(0,active);
    }
    @Test
    public void ActivityOfAnUser() throws UserDoesNotExistException, MoreThanOneActiveGame, GameDoesNotExistException, NoActiveGameException, CloneNotSupportedException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.getActivityOfUser("JEJEJ",idGame1));
        this.gm.StartGame(idAlba,idGame1);
        this.gm.ChangeOfLevel(idAlba,idGame1);
        this.gm.ChangeOfLevel(idAlba,idGame1);
        List<Activity> activity=this.gm.getActivityOfUser(idAlba,idGame1);
        Assert.assertEquals(2,activity.size());
    }

    @Test
    public void UserByPoints() throws GameDoesNotExistException, MoreThanOneActiveGame, UserDoesNotExistException, NoActiveGameException{
        Assert.assertThrows(GameDoesNotExistException.class, ()->this.gm.StartGame(idAlba,"Juanete"));
        this.gm.StartGame(idAlba,idGame1);
        this.gm.StartGame(idPaula,idGame1);
        List<User> listUsersSorted=this.gm.usersByPoints(idGame1);
        Assert.assertEquals("Alba",listUsersSorted.get(0).getName());
        Assert.assertEquals("Paula",listUsersSorted.get(1).getName());
    }








}


