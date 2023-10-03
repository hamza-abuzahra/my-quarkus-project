package com.example.application.usecases.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService implements GetUserByIdUseCase, CreateUserUseCase, LoginUseCase {

    @Inject
    private IUserRepository userRepository;

    @RestClient
    private LoginProxy loginClient;

    Keycloak keycloak;

    Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public String createUser(User user, List<String> roles) {
        keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:9090")
            .realm("master")
            .clientId("test-client")
            .clientSecret("J1a22JvKE7NvRQrhp1dKcROarAcDerNi")
            .username("admin")
            .password("admin")
            .grantType("password")
            .build();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getFname()+user.getLname());
        userRepresentation.setFirstName(user.getFname());
        userRepresentation.setLastName(user.getLname());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setRealmRoles(roles);
        userRepresentation.setEnabled(true);
        
        Response ceratedUserRes = keycloak.realm("Demo").users().create(userRepresentation);
        if (ceratedUserRes.getStatus() == 201) {
            List<RoleRepresentation> roleReps = roleRepresentationMapper(roles);
            URI location = ceratedUserRes.getLocation();
            String id = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
            keycloak.realm("Demo").users().get(id).roles().realmLevel().add(roleReps);
            userRepository.createUser(user);
        }
        return ceratedUserRes.readEntity(String.class);
    }

    @Override
    public String login(UserCredentialsRequest credentials) {        
        try{
            Response response = loginClient.getToken("market", "VRh4d0zTS7VMikxl0dgyv8MHzuZQhIB8", credentials.getUsername(), credentials.getPassword(), "password");
            return response.readEntity(String.class);
        } catch (ClientWebApplicationException e){
            return null;
        }
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
