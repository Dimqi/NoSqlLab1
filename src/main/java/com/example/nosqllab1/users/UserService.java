package com.example.nosqllab1.users;

import com.example.nosqllab1.models.User;
import com.example.nosqllab1.repository.UserRepository;
import com.example.nosqllab1.riakservices.RiakCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RiakCounterService riakCounterService;
    private final PasswordEncoder passwordEncoder;


    public void deleteUserById(Long id) {
        userRepository.delete(String.valueOf(id));
    }


    public UserResponse createUser(UserRequest userRequest) {
        List<User> users = userRepository.findAll();
        boolean userExist = users.stream()
                .anyMatch(user -> user.getName() != null && user.getName().equalsIgnoreCase(userRequest.name()));

        if (userExist) {
            throw new UserAlreadyExistsException("user already exist");
        }

        User user = new User();
        long id = incrementUserCounter();
        user.setId(String.valueOf(id));
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setRole("ПРЕПОДАВАТЕЛЬ");

        userRepository.save(user);

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

    private long incrementUserCounter(){
        try {
            return riakCounterService.generateNextId(User.class);
        }catch (ExecutionException| InterruptedException e){
            throw  new RuntimeException("error upgrade user counter");
        }
    }
}
