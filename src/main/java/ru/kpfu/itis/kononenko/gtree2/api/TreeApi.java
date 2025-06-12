package ru.kpfu.itis.kononenko.gtree2.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeFormRequest;

import java.util.Map;

@RestController
@RequestMapping("/trees")
public interface TreeApi {

    @PostMapping
    ResponseEntity<Long> create(
            @AuthenticationPrincipal
            String jwt,
            @RequestBody
            TreeFormRequest dto
    );

    @GetMapping("/{id}/nodes")
    Map<String, String> json(@PathVariable Long id);
}
