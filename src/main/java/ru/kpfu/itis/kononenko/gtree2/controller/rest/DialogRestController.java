package ru.kpfu.itis.kononenko.gtree2.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.MessageRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.MessageResponse;
import ru.kpfu.itis.kononenko.gtree2.service.DialogService;

import java.util.List;

@RestController
@RequestMapping("/dialog/api")
@RequiredArgsConstructor
public class DialogRestController {

    private final DialogService dialogService;

    @GetMapping("/{username}")
    public List<MessageResponse> getMessages(@PathVariable String username) {
        return dialogService.getMessages(username);
    }

    @PostMapping("/{username}")
    public MessageResponse sendMessage(@PathVariable String username,
                                       @RequestBody MessageRequest request) {
        return dialogService.sendMessage(username, request.content());
    }
}