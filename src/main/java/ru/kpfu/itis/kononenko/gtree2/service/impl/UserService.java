package ru.kpfu.itis.kononenko.gtree2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Conversation;
import ru.kpfu.itis.kononenko.gtree2.entity.ERole;
import ru.kpfu.itis.kononenko.gtree2.entity.Role;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.exception.NotFoundException;
import ru.kpfu.itis.kononenko.gtree2.mapper.UserMapper;
import ru.kpfu.itis.kononenko.gtree2.repository.*;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConversationRepository conversationRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final CacheManager cacheManager;
    private final PrivateMessageRepository messageRepository;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;


    @Transactional
    public User save(final UserRegisterRequest request) {

        User user = userMapper.toUser(request);
        user.setPasswordHash(encoder.encode(request.password()));
        user.setEmailVerified(false);

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Error: Role is not found."));

        user.getRoles().add(userRole);

        Objects.requireNonNull(cacheManager.getCache("users")).evict(user.getUsername());
        return userRepository.save(user);
    }

    public List<UserResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getCurrent() {
        User currentUser = getAuthenticatedUser();
        return userMapper.toUserResponse(currentUser);
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким username нет"));
        return userMapper.toUserResponse(user);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            throw new RuntimeException("Пользователь не авторизован или auth == null");
        }
        return details.getUser();
    }

    @Transactional
    public UserResponse updateCurrent(UserRequest request) {
        User user = getAuthenticatedUser();
        String oldUsername = user.getUsername();
        String oldEmail = user.getEmail();
        String newEmail = request.email();

        userMapper.update(user, request);

        if (!oldEmail.equals(newEmail)) {
            user.setEmailVerified(false);
        }
        userRepository.save(user);
        Objects.requireNonNull(cacheManager.getCache("users")).evict(oldUsername);
        Objects.requireNonNull(cacheManager.getCache("users")).evict(user.getUsername());
        return userMapper.toResponse(user);
    }

    public List<User> findAllVerified(){
        return userRepository.findAllByEmailVerifiedTrue();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteByUsername(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        beforeUserDelete(user);
        userRepository.deleteByUsername(username);

        Objects.requireNonNull(cacheManager.getCache("users")).evict(username);
    }

    private void beforeUserDelete(User user) {
        List<Conversation> conversations = conversationRepository.findByUser1OrUser2(user, user);
        for (Conversation conv : conversations) {
            messageRepository.deleteAllByConversation(conv);
        }
        conversationRepository.deleteAll(conversations);
        refreshTokenService.invalidate(user.getUsername());
        verificationTokenService.deleteAllByUser(user);
    }

    @Transactional
    public void deleteCurrent() {
        User user = getAuthenticatedUser();
        beforeUserDelete(user);
        userRepository.delete(user);
        Objects.requireNonNull(cacheManager.getCache("users")).evict(user.getUsername());
    }
}

