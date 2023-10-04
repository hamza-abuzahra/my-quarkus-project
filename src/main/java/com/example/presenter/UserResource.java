package com.example.presenter;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.application.usecases.user.UserCredentialsRequest;
import com.example.application.usecases.user.CreateUserRequest;
import com.example.application.usecases.user.CreateUserUseCase;
import com.example.application.usecases.user.GetUserByIdUseCase;
import com.example.application.usecases.user.LoginUseCase;
import com.example.domain.User;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("api/users")
@Authenticated
public class UserResource {

    @Inject
    private CreateUserUseCase createUserUseCase;
    @Inject
    private GetUserByIdUseCase getUserByIdUseCase;
    @Inject
    private LoginUseCase loginUseCase;

    @Inject 
    SecurityIdentity securityIdentity;

    Logger logger = Logger.getLogger(UserResource.class.getName());

    @GET
    @Path("/{id}")
    @PermitAll
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    @PermitAll
    public Response login(@Valid UserCredentialsRequest userRequest){
        logger.info(securityIdentity.isAnonymous() + "");
        String accessToken = loginUseCase.login(userRequest);
        if (accessToken != null){
            return Response.ok(accessToken).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response createUser(@Valid CreateUserRequest createUserRequest) {
        try {
            String result = createUserUseCase.createUser(createUserRequest.getUser(), createUserRequest.getRoles()); 
            if (result.length() > 0){
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
}
