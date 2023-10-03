package com.example.application.usecases.user;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.domain.IUserRepository;
import com.example.domain.IdentityServiceInterface;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService implements GetUserByIdUseCase, CreateUserUseCase, LoginUseCase {

    @Inject
    private IUserRepository userRepository;

    @Inject 
    private IdentityServiceInterface identityService;


    Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public String createUser(User user, List<String> roles) {
        // UserRepresentation userRepresentation = new UserRepresentation();
        // userRepresentation.setUsername(user.getFname()+user.getLname());
        // userRepresentation.setFirstName(user.getFname());
        // userRepresentation.setLastName(user.getLname());
        // userRepresentation.setEmail(user.getEmail());
        // userRepresentation.setEnabled(true);
        
        // Response ceratedUserRes = keycloak.realm("Demo").users().create(userRepresentation);
        // if (ceratedUserRes.getStatus() == 201) {
        //     List<RoleRepresentation> roleReps = roleRepresentationMapper(roles);
        //     URI location = ceratedUserRes.getLocation();
        //     String id = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
        //     keycloak.realm("Demo").users().get(id).roles().realmLevel().add(roleReps);
        // }
        String res = identityService.createUser(user, roles);
        if (res.length() > 0){
            return res;
        }
        userRepository.createUser(user);
        return "";
    }

    @Override
    public String login(UserCredentialsRequest credentials) {        
        // try{
        //     Response response = loginClient.getToken("market", "VRh4d0zTS7VMikxl0dgyv8MHzuZQhIB8", credentials.getUsername(), credentials.getPassword(), "password");
        //     return response.readEntity(String.class);
        // } catch (ClientWebApplicationException e){
        //     return null;
        // }
        return identityService.getToken("market", "VRh4d0zTS7VMikxl0dgyv8MHzuZQhIB8", "password", credentials.getUsername(), credentials.getPassword());
    }
}
