package ru.kpfu.itis.kononenko.gtree2.controller.handler;

import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestValidationHandler {

    // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ResponseEntity<Map<String,String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String,String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errors);
    }

    // 401
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Map<String,Object> handleAuthFailures() {
        return Map.of(
                "success", false,
                "message", "Неверный логин или пароль"
        );
    }

    //500
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String,Object> handleAll(Exception ex) {
//        return Map.of(
//                "success", false,
//                "message", "Внутренняя ошибка сервера"
//        );
//    }

}
