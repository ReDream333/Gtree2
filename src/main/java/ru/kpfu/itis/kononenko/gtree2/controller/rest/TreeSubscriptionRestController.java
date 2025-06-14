package ru.kpfu.itis.kononenko.gtree2.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.service.TreeSubscriptionService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

import java.util.Map;

@RestController
@RequestMapping("/api/trees/{treeId}/subscribe")
@RequiredArgsConstructor
public class TreeSubscriptionRestController {

    private final TreeSubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<?> subscribe(@PathVariable Long treeId,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        if (Boolean.FALSE.equals(user.getUser().getEmailVerified())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "EMAIL_NOT_VERIFIED"));
        }
            subscriptionService.subscribe(treeId, user.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> unsubscribe(@PathVariable Long treeId,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        subscriptionService.unsubscribe(treeId, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Boolean>> check(@PathVariable Long treeId,
                                                      @AuthenticationPrincipal CustomUserDetails user) {
        boolean subscribed = subscriptionService.isSubscribed(treeId, user.getUser().getId());
        return ResponseEntity.ok(Map.of("subscribed", subscribed));
    }
}