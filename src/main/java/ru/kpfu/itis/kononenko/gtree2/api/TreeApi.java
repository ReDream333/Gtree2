package ru.kpfu.itis.kononenko.gtree2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeFormRequest;

import java.util.Map;

@RestController
@RequestMapping("/trees")
@Tag(name = "Trees")
public interface TreeApi {

    @Operation(summary = "Create tree")
    @PostMapping
    ResponseEntity<Long> create(
            @AuthenticationPrincipal
            String jwt,
            @RequestBody
            TreeFormRequest dto
    );

    @Operation(summary = "Get tree nodes as JSON")
    @GetMapping("/{id}/nodes")
    Map<String, String> json(@PathVariable Long id);
}
