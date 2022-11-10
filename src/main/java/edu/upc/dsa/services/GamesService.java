package edu.upc.dsa.services;
import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exceptions.EmailAlreadyBeingUsedException;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javafx.scene.chart.ScatterChart;


import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/", description = "Endpoint to Track Service")
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
    @ApiOperation(value = "create a new User", notes = "Do you want to register to our shop?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= UserInformation.class),
            @ApiResponse(code = 409, message = "This user already exists."),
            @ApiResponse(code = 500, message = "Empty credentials")

    })

    @Path("/createUser")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response newUser(UserInformation newUser){

        try{
            this.tm.addUser(newUser.getName(), newUser.getSurname(), newUser.getBirthDate(), newUser.getMail(), newUser.getPassword());
            return Response.status(201).entity(newUser).build();
        }
        catch (EmailAlreadyBeingUsedException E){
            return Response.status(409).entity(newUser).build();
        }


    }
    @POST
    @ApiOperation(value = "Log In to the shop", notes = "Do you want to log in to our shop?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= UserInformation.class),
            @ApiResponse(code = 409, message = "Wrong credentials.")


    })
    @Path("/logIn")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response logIn(Credentials credentials){

        try{
            this.tm.LogIn(credentials);
            return Response.status(201).entity(credentials).build();
        }
        catch (IncorrectCredentialsException E){
            return Response.status(409).entity(credentials).build();
        }
    }
    @GET
    @ApiOperation(value = "get a Product", notes = "Choose a product of our shop!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String id) {
        Product t = this.tm.getProduct(id);
        if (t == null) return Response.status(404).build();
        else  return Response.status(201).entity(t).build();
    }

    @GET
    @ApiOperation(value = "get all the Products sorted", notes = "Get the shop list sorted!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/productsSorted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsSorted() {
        List<Product> productSortedByPrice = this.tm.objectsByPrice();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(productSortedByPrice) {};
        return Response.status(201).entity(entity).build();
    }
    @GET
    @ApiOperation(value = "get all the Users sorted", notes = "Get the users list sorted!")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/UsersSorted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersSorted() {
        List<User> usersSorted = this.tm.userByAlfabet();
        if(usersSorted==null){
            return Response.status(404).entity(usersSorted).build();
        }
        List<UsersWithoutPassword> usersWithoutPassword= this.tm.DisplayingUsersInfo(usersSorted);
        GenericEntity<List<UsersWithoutPassword>> entity = new GenericEntity<List<UsersWithoutPassword>>(usersWithoutPassword) {};
        return Response.status(201).entity(entity).build();
    }



    @DELETE
    @ApiOperation(value = "delete a Product", notes = "Do you wanna erase a product from the shop?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/{id}")
    public Response deleteTrack(@PathParam("id") String id) {
        Object t = this.tm.getProduct(id);
        if (t == null) return Response.status(404).build();
        else this.tm.deleteProduct(id);
        return Response.status(201).build();
    }
    @GET
    @ApiOperation(value = "get a Products bought by an User", notes = "How much have you bougth from us?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Object.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/productsByUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByUser(@PathParam("id") String id) {
        List<Product> productsBoughtByUser = this.tm.productsBoughtByTheUser(id);
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(productsBoughtByUser) {};
        return Response.status(201).entity(entity).build();
    }

    @PUT
    @ApiOperation(value = "update a Product", notes = "Do you want to update a product?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/")
    public Response updateProduct(Product product) {

        Product t = this.tm.updateProduct(product);
        if (t == null) return Response.status(404).build();
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "Purchase a product", notes = "Do you want to update a product?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 403, message = "Not enough money"),
            @ApiResponse(code = 404, message = "Not found any object or user with that id"),
    })
    @Path("/PurchaseOfProduct/{idUser}/{idProduct}")
    public Response productBoughtByUser(@PathParam("idUser") String idUser, @PathParam("idProduct") String idProduct) {
        try{
            this.tm.productBought(idUser,idProduct);
            return Response.status(201).build();
        }
        catch(UserHasNotMoneyException e){
            return Response.status(403).entity(idUser).build();
        }
        catch(UserDoesNotExistException | ProductDoesNotExistException e ){
            return Response.status(404).entity(idUser).build();
        }

    }

    @POST
    @ApiOperation(value = "add a new Product", notes = "Do you want to contribute with a product to our shop?")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Object.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newProduct(Product newProduct) {

        if (newProduct.getName()==null || newProduct.getDescription()==null)
            return Response.status(500).entity(newProduct).build();
        this.tm.addProduct(newProduct.getName(), newProduct.getDescription(), newProduct.getPrice());
        return Response.status(201).entity(newProduct).build();

    }


}
