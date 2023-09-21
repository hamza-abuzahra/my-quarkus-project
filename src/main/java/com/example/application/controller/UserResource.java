package com.example.application.controller;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.application.usecases.CreateUserUseCase;
import com.example.application.usecases.GetUserByIdUseCase;
import com.example.domain.User;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("api/users")
public class UserResource {

    @Inject
    private CreateUserUseCase createUserUseCase;
    @Inject
    private GetUserByIdUseCase getUserByIdUseCase;
    
    Logger logger = Logger.getLogger(OrderResource.class.getName());

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id){
        try{            
            Optional<User> resOrder = getUserByIdUseCase.getUserById(id);
            if (resOrder.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "User not found")).build(); 
            }
            return Response.ok(resOrder.get()).build();
        } catch (Exception e){

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    public Response createUser(@Valid User user){
        try {
            if (user.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "id must be null")).build();
            }
            if (!createUserUseCase.createUser(user)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "one of the given fields do not exist, either user or one of the products")).build();
            } 
            return Response.ok("User added successfully").build();
            
        } catch (Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
}
