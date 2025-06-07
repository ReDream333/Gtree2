package ru.kpfu.itis.kononenko.gtree2.controller.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeBiographyService;

@Controller
@RequestMapping("/biography")
@RequiredArgsConstructor
public class BiographyController {

    private final NodeRepository nodeRepository;
    private final NodeBiographyService biographyService;

    @GetMapping
    public String biographyPage(@RequestParam("nodeId") Long nodeId, Model model) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        model.addAttribute("nodeId", nodeId);
        model.addAttribute("nodeName", node.getFirstName() + " " + node.getLastName());
        model.addAttribute("biography", biographyService.getBiography(nodeId));
        return "biography";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> saveBiography(@RequestParam Long nodeId,
                                              @RequestParam String biography) {
        biographyService.saveBiography(nodeId, biography);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> deleteBiography(@RequestParam Long nodeId) {
        biographyService.deleteBiography(nodeId);
        return ResponseEntity.noContent().build();
    }
}