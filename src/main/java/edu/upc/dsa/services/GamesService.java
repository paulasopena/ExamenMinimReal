
package edu.upc.dsa.services;
import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.TransferObjects.GameInformation;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Activity;
import edu.upc.dsa.models.Departure;
import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.jws.soap.SOAPBinding;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javafx.scene.chart.ScatterChart;
import org.eclipse.persistence.annotations.Convert;


import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/games", description = "Endpoint to Track Service")
@Path("/games")
public class GamesService {
    private GameManager tm;
    public GamesService() throws EmailAlreadyBeingUsedException {
        this.tm = GameManagerImpl.getInstance();
        if (tm.size()==0) {
            this.tm.addUser("Alba","Roma","albaroma@gmail.com","123456");
            this.tm.addUser("Maria","Ubiergo","meri@gmail.com","123456");
            this.tm.addUser("Guillem","Purti","guille@gmail.com","123456");
        }
    }
    @POST
    @ApiOperation(value = "create a new Game", notes = "Do you want to create a Game?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 409, message = "This user already exists."),
            @ApiResponse(code = 500, message = "Empty credentials")

    })

    @Path("/createGame")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response newUser(GameInformation newGame){
        String idGame=this.tm.addGame(newGame.getName(), newGame.getDescription(), 5);
        if(idGame==null){
            return Response.status(500).entity(newGame).build();
        }
        return Response.status(201).entity(newGame).build();
    }
    @POST
    @ApiOperation(value = "Starting Game", notes = "Do you want to start gaming?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Departure.class),
            @ApiResponse(code = 404, message = "Not found.")

    })
    @Path("/StartGame/{idUser}/{idGame}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response StartGame(@PathParam("idUser") String idUser, @PathParam("idGame") String idGame){

        try{
            this.tm.StartGame(idUser,idGame);
            String idDeparture=this.tm.getUsers().get(idUser).getGames().get(idGame).getLastDeparture().getIdDeparture();
            return Response.status(201).entity(idDeparture).build();
        }
        catch (MoreThanOneActiveGame e) {
            return Response.status(403).entity(idGame).build();
        }
        catch (UserDoesNotExistException | GameDoesNotExistException E) {
            return Response.status(404).entity(idGame).build();
        }

    }

    @GET
    @ApiOperation(value = "get a Actual Level", notes = "get the level of your game play!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code =403, message = "No Active")
    })
    @Path("/ActualLevel/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActualLevel(@PathParam("id") String idUser) {
        try{
            int actualLevel=this.tm.ActualLevel(idUser);
            String actualLevels="Level "+actualLevel;
            return Response.status(201).entity(actualLevels).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idUser).build();
        }
        catch(NoActiveGameException e){
            return Response.status(403).entity(idUser).build();
        }
    }

    @GET
    @ApiOperation(value = "get all the Actual Points", notes = "Do you wanna know your score man?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code =403, message = "No Active")
    })
    @Path("/getPoints/{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActualPoints(@PathParam("idUser") String idUser) {
        try{
            int actualPoints=this.tm.ActualPoints(idUser);
            String actualPoint="Actual points"+actualPoints;
            return Response.status(201).entity(actualPoint).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idUser).build();
        }
        catch(NoActiveGameException e){
            return Response.status(403).entity(idUser).build();
        }
    }
    @PUT
    @ApiOperation(value = "Change of Level", notes = "Let's change your level!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code =403, message = "No Active")
    })
    @Path("/ChangeOfLevel/{idUser}/{idGame}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ChangeOfLevel(@PathParam("idUser") String idUser, @PathParam("idGame") String idGame) {
        try{
            Activity activity=this.tm.ChangeOfLevel(idUser,idGame);
            String response=activity.getIdUser()+" "+activity.getPoints()+" "+activity.getDate();
            return Response.status(201).entity(response).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idUser).build();
        }
        catch (NoActiveGameException e){
            return Response.status(403).entity(idGame).build();
        }
    }
    @PUT
    @ApiOperation(value = "End Game", notes = "Do you wanna end the game?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code =403, message = "No Active")
    })
    @Path("/EndGame/{idUser}/{idGame}")
    public Response EndingGame(@PathParam("idUser") String idUser, @PathParam("idGame") String idGame) {
        try{
            this.tm.EndingGame(idUser,idGame);
            String response="User "+idUser+"has ended the game with ID "+idGame;
            return Response.status(201).entity(response).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idUser).build();
        }
        catch (NoActiveGameException e){
            return Response.status(403).entity(idGame).build();
        }
    }
    @GET
    @ApiOperation(value = "get a Products bought by an User", notes = "How much have you bougth from us?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/ActivityOfAGame/{idUser}/{idGame}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityOfUserAndGame(@PathParam("idUser") String idUser,@PathParam("idGame") String idGame) {
        try{
            List<Activity> listOfActivity = this.tm.getActivityOfUser(idUser,idGame);
            GenericEntity<List<Activity>> entity = new GenericEntity<List<Activity>>(listOfActivity) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idGame).build();
        }
    }
    @GET
    @ApiOperation(value = "Order the users by points!", notes = "Who is the winner?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/UsersByPoints/{idGame}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByPoints(@PathParam("idGame") String idGame) {
        try{
            List<User> listOfUsers = this.tm.usersByPoints(idGame);
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(listOfUsers) {};
            return Response.status(201).entity(entity).build();
        }
        catch(GameDoesNotExistException e){
            return Response.status(404).entity(idGame).build();
        }
    }
    @GET
    @ApiOperation(value = "get all Partidas", notes = "get the level of your game play!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Game not found")

    })
    @Path("/AllGamesUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGamesUser(@PathParam("id") String idUser) {
        try{
            List<Departure> listOfPartidasOfAllGames = this.tm.getGames(idUser);
            GenericEntity<List<Departure>> entity = new GenericEntity<List<Departure>>(listOfPartidasOfAllGames) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserDoesNotExistException e){
            return Response.status(404).entity(idUser).build();
        }

    }
}


