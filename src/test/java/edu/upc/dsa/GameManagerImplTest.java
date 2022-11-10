package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
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
    String idJacket;
    String idShoe;
    String idTrouser;
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
    public void UserStartingAGame() throws UserDoesNotExistException, GameDoesNotExistException, MoreThanOneActiveGame {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.StartGame("JEJEJ",idGame1));
        Assert.assertThrows(GameDoesNotExistException.class, ()->this.gm.StartGame(idAlba,"Juanete"));
        this.gm.StartGame(idAlba,idGame1);
        Assert.assertEquals(1,this.gm.getUsers().get(idAlba).getGameWithID(idGame1).getActive());
    }
    @Test
    public void ActualLevel() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        Assert.assertThrows(NoActiveGameException.class, ()->this.gm.ActualLevel(idAlba));
        UserStartingAGame();
        Assert.assertEquals(0,this.gm.ActualLevel(idAlba));
    }
    @Test
    public void ActualPoints() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        Assert.assertThrows(NoActiveGameException.class, ()->this.gm.ActualLevel(idAlba));
        UserStartingAGame();
        Assert.assertEquals(50,this.gm.ActualPoints(idAlba));
    }
    @Test
    public void ChangeOfLevel() throws UserDoesNotExistException, NoActiveGameException, MoreThanOneActiveGame, GameDoesNotExistException {
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        Assert.assertThrows(NoActiveGameException.class, ()->this.gm.ActualLevel(idAlba));
        UserStartingAGame();
        Assert.assertEquals(1,this.gm.ChangeOfLevel(idAlba));
    }
    @Test
    public void GamesOfUser() throws UserDoesNotExistException, MoreThanOneActiveGame, GameDoesNotExistException {
        UserStartingAGame();
        Assert.assertThrows(UserDoesNotExistException.class, ()->this.gm.ActualLevel("JEJEJ"));
        List<Game> listGames=this.gm.getGamesOfUser(idAlba);
        Assert.assertEquals(1,listGames.size());

    }
}
