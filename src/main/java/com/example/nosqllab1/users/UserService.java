package com.example.nosqllab1.users;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private ArrayList<UserResponse> users = new ArrayList<>(List.of(
            new UserResponse(1L, "Петя", "petyadominator2015@gmail.com"),
            new UserResponse(2L, "Дж. Эпштейн", "prettyisand@yandex.ru")
    ));

    public void deleteUserById(Long id) {
        boolean found = users.removeIf(user -> user.id().equals(id));
        if (!found) {
            throw new UserNotFoundException(String.format("User with id %s not found", id));
        }
        //тут удаление из бд
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        boolean found = users.removeIf(user -> user.id().equals(id));
        if (found) {
            UserResponse userResponse = new UserResponse(id,
                    userRequest.name(),
                    userRequest.email());
            users.add(userResponse);
            return userResponse;
        }
        throw new UserNotFoundException(String.format("User with id %s not found", id));
        //тут обновление в бд
    }

    public UserResponse createUser(UserRequest userRequest) {
        UserResponse userResponse = users.stream()
                .filter(user -> user.name().equals(userRequest.name()))
                .findFirst()
                .orElse(null);
        if (userResponse == null) {
            UserResponse ur = new UserResponse(users.getLast().id() + 1,
                    userRequest.name(),
                    userRequest.email());
            users.add(ur);
            return ur;
        }
        throw new UserAlreadyExistsException(String.format("User with name %s already exists", userRequest.name()));
        //тут добавление в бд
    }

    public UserResponse getUserById(Long id) {
        UserResponse userResponse = users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst()
                .orElse(null);
        if (userResponse == null) {
            throw new UserNotFoundException(String.format("User with id %s not found", id));
        }
        return userResponse;
        //тут поиск в бд вместо листа
    }

    public List<UserResponse> getUsers() {
        return users;
    }
}
