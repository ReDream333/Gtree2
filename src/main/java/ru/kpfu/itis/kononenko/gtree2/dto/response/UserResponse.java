package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;
import ru.kpfu.itis.kononenko.gtree2.entity.User;

@Builder
public record UserResponse(
        String username,
        String email
) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getUsername(), user.getEmail());
    }
}
