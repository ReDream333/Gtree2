package ru.kpfu.itis.kononenko.gtree2.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
import ru.kpfu.itis.kononenko.gtree2.validation.ValidFileSize;

import java.util.List;

@RequestMapping("/users/api")
@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")???
public interface UserApi {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserResponse> getAll(
            @PositiveOrZero(message = "offset должен быть неотрицательным")
            @RequestParam(name = "offset", required = false, defaultValue = "0")
            Integer offset,
            @Positive(message = "limit должен быть больше 0")
            @RequestParam(name = "limit", required = false, defaultValue = "10")
            Integer limit
    );

    @GetMapping("/me")
    UserResponse getCurrent();

    @PutMapping("/me")
    UserResponse updateCurrent(
            @RequestBody
            @Valid
            UserRequest request
    );

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCurrent();

    @PutMapping("/me/avatar")
    String updateCurrentAvatar(
            @RequestPart("file")
            @ValidFileSize(maxBytes = 10 * 1024 * 1024, message = "Размер аватарки не должен превышать 10 МБ")
            MultipartFile file
    );

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse getByUsername(
            @PathVariable
            String username
    );


    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUsername(
            @PathVariable("username")
            String login
    );
}
