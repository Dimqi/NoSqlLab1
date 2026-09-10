package com.example.nosqllab1.users;

import com.example.nosqllab1.models.User;

public record UserResponse(Long id, String name, String email) {
    public static UserResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }

        Long parsedId = null;
        if (user.getId() != null) {
            try {
                parsedId = Long.parseLong(user.getId());
            } catch (NumberFormatException e) {
                parsedId = null;
            }
        }

        return new UserResponse(
                parsedId,
                user.getName(),
                user.getEmail()
        );
    }
}
