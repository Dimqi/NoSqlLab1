package com.example.nosqllab1.users;

import com.example.nosqllab1.models.User;
import com.example.nosqllab1.repository.UserRepository;
import com.example.nosqllab1.riakservices.RiakCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RiakCounterService riakCounterService;


    public void deleteUserById(Long id) {
        userRepository.delete(String.valueOf(id));
    }


    public UserResponse createUser(UserRequest userRequest) {
        List<User> users = userRepository.findAll();
        Boolean userExist = users.stream()
                .map(user -> user.getName().equals(userRequest.name()))
                .findFirst()
                .isPresent();
        if(userExist){
            throw new UserAlreadyExistsException("user already exist");
        }

       User user = new User();
       long id = incrementeUserCounter();
       user.setId(String.valueOf(id));
       user.setName(userRequest.name());
       user.setEmail(userRequest.email());
       user.setPassword(userRequest.password());

       return UserResponse.fromEntity(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(user -> UserResponse.fromEntity(user))
                .toList();

        return userResponses;
    }

    private long incrementeUserCounter(){
        try {
            return riakCounterService.generateNextId(User.class);
        }catch (ExecutionException| InterruptedException e){
            throw  new RuntimeException("error upgrade user counter");
        }
    }
}
