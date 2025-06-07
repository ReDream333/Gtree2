package ru.kpfu.itis.kononenko.gtree2.controller.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.kononenko.gtree2.api.UserApi;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
import ru.kpfu.itis.kononenko.gtree2.service.impl.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public List<UserResponse> getAll(Integer offset, Integer limit) {
        return userService.getAll(offset, limit);
    }

    @Override
    public UserResponse getCurrent() {
        return userService.getCurrent();
    }

    @Override
    public UserResponse updateCurrent(UserRequest request) {
        return userService.updateCurrent(request);
    }

    @Override
    public void deleteCurrent() {
        userService.deleteCurrent();
    }

    @Override
    public String updateCurrentAvatar(MultipartFile file) {
        return "";
    }

    @Override
    public UserResponse getByUsername(String username) {
        return userService.getByUsername(username);
    }

    @Override
    public void deleteByUsername(String username) {
        userService.deleteByUsername(username);
    }
}
