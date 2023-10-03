package com.example.infrastructure.keycloak;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.example.domain.IdentityServiceInterface;
import com.example.domain.User;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class KeycloakServiceImplementation implements IdentityServiceInterface {

    @RestClient
    private LoginProxy loginClient;

    Keycloak keycloak;

    @PostConstruct
    public void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:9090")
            .realm("master")
            .clientId("test-client")
            .clientSecret("J1a22JvKE7NvRQrhp1dKcROarAcDerNi")
            .username("admin")
            .password("admin")
            .grantType("password")
            .build();
    }

    @PreDestroy
    public void closeKeycloak() {
        keycloak.close();
    }

    @Override
    public String getToken(String clientId, String clientSecret, String grantType, String username, String password) {
        try{
            Response response = loginClient.getToken(clientId, clientSecret, username, password, grantType);
            return response.readEntity(String.class);
        } catch (ClientWebApplicationException e){
            return null;
        }
    }

    @Override
    public String createUser(User user, List<String> roles) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getFname()+user.getLname());
        userRepresentation.setFirstName(user.getFname());
        userRepresentation.setLastName(user.getLname());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        
        Response ceratedUserRes = keycloak.realm("Demo").users().create(userRepresentation);
        if (ceratedUserRes.getStatus() == 201) {
            List<RoleRepresentation> roleReps = roleRepresentationMapper(roles);
            URI location = ceratedUserRes.getLocation();
            String id = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
            keycloak.realm("Demo").users().get(id).roles().realmLevel().add(roleReps);
        }
        return ceratedUserRes.readEntity(String.class);
    }

    private List<RoleRepresentation> roleRepresentationMapper(List<String> roles){
        List<RoleRepresentation> allRealmRoles = keycloak.realm("Demo").roles().list();
        List<RoleRepresentation> finalRealmRoles = new ArrayList<RoleRepresentation>();
        for (String role : roles) {
            boolean roleExists = allRealmRoles.stream().anyMatch(realmRole -> realmRole.getName().equals(role));
            if (roleExists) {
                RoleResource roleRep = keycloak.realm("Demo").roles().get(role);
                finalRealmRoles.add(roleRep.toRepresentation());
            }
        }
        return finalRealmRoles;
    }
 
}
