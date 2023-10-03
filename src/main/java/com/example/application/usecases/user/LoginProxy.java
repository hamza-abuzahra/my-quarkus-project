package com.example.application.usecases.user;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/realms/")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(baseUri = "http://localhost:9090")
public interface LoginProxy {
    @POST
    @Path("/Demo/protocol/openid-connect/token")
    public Response getToken(@FormParam("client_id") String clientId,
                    @FormParam("client_secret") String clientSecret,
                    @FormParam("username") String username,
                    @FormParam("password") String password,
                    @FormParam("grant_type") String grantType);
}
