package ru.kpfu.itis.kononenko.gtree2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.aop.Timed;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.mapper.UserMapper;
import ru.kpfu.itis.kononenko.gtree2.repository.RoleRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Timed("service.saveUser")
    public User save(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByUsername(userRegisterRequest.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegisterRequest.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(userRegisterRequest);
        user.setPasswordHash(encoder.encode(userRegisterRequest.password()));
        return userRepository.save(user);
    }

    public List<User> findAllVerified(){
        return userRepository.findAllByEmailVerifiedTrue();
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::fromUser).toList();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

