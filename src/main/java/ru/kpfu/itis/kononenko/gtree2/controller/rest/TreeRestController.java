package ru.kpfu.itis.kononenko.gtree2.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.kononenko.gtree2.dto.response.TreeResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;
import ru.kpfu.itis.kononenko.gtree2.mapper.TreeMapper;
import ru.kpfu.itis.kononenko.gtree2.service.TreeService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
@RequiredArgsConstructor
public class TreeRestController {

    private final TreeService treeService;
    private final TreeMapper treeMapper;

    @GetMapping("/public")
    public ResponseEntity<List<TreeResponse>> getPublicTrees() {
        List<Tree> trees = treeService.getPublicTrees();
        return ResponseEntity.ok(trees.stream().map(treeMapper::toResponse).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<TreeResponse>> getMyTrees(@AuthenticationPrincipal CustomUserDetails user) {
        Long userId = user.getUser().getId();
        List<Tree> trees = treeService.getTreesByUserId(userId);
        return ResponseEntity.ok(trees.stream().map(treeMapper::toResponse).toList());
    }
}