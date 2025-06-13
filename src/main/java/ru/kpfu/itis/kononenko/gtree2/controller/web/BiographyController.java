package ru.kpfu.itis.kononenko.gtree2.controller.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeBiographyService;
import ru.kpfu.itis.kononenko.gtree2.service.WikipediaService;

@Slf4j
@Controller
@RequestMapping("/biography")
@RequiredArgsConstructor
public class BiographyController {

    private final NodeRepository nodeRepository;
    private final NodeBiographyService biographyService;
    private final WikipediaService wikipediaService;

    @GetMapping
    public String biographyPage(@RequestParam("nodeId") Long nodeId, Model model) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        model.addAttribute("nodeId", nodeId);
        model.addAttribute("nodeName", node.getFirstName() + " " + node.getLastName());
        model.addAttribute("biography", biographyService.get(nodeId));
        return "biography";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> saveBiography(@RequestParam Long nodeId,
                                              @RequestParam String biography) {
        biographyService.save(nodeId, biography);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> deleteBiography(@RequestParam Long nodeId) {
        biographyService.delete(nodeId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/auto")
    @ResponseBody
    public ResponseEntity<java.util.List<String>> autoBiography(@RequestParam Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        log.info("Auto biography for node name and lastname {} {}", node.getFirstName(), node.getLastName());

        return ResponseEntity.ok(wikipediaService.fetchBiographies(node.getFirstName(), node.getLastName()));
    }
}