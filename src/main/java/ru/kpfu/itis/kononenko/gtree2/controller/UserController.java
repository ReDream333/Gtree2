//package ru.kpfu.itis.kononenko.gtree2.controller;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
//import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
//import ru.kpfu.itis.kononenko.gtree2.service.UserService;
//
//import java.util.List;
//
//@Controller
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @ResponseBody
//    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<UserResponse> getUsers() {
//        return userService.findAll();
//    }
//
//    @PostMapping("/user")
//    public String createUser(@RequestBody UserRegisterRequest userRequest, HttpServletRequest request) {
//        String url = request.getRequestURL().toString().replace(request.getServletPath(), "");
//        userService.save(userRequest);
//        return "sign-success";
//    }
//
//    @GetMapping("/verify")
//    public String verifyUser(@RequestParam("code") String code, Model model) {
////        boolean verified = userService.verifyUser(code);
//        model.addAttribute("verified", verified);
//        return "verification_result";
//    }
//
//    @GetMapping("/sign_up")
//    public String showSignUpForm() {
//        return "sign_up"; // JSP для формы регистрации
//    }
//}
