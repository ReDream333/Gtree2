package ru.kpfu.itis.kononenko.gtree2.controller.web;

import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.entity.NodePhoto;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodePhotoService;

import java.util.Map;

@Controller
@RequestMapping("/album")
@RequiredArgsConstructor
public class PhotoAlbumController {

    private final NodeRepository nodeRepository;
    private final NodePhotoService photoService;

    @GetMapping
    public String albumPage(@RequestParam("nodeId") Long nodeId, Model model) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        model.addAttribute("nodeId", nodeId);
        model.addAttribute("nodeName", node.getFirstName() + " " + node.getLastName());
        model.addAttribute("photos", photoService.getPhotos(nodeId));
        return "photo-album";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<NodePhoto> addPhoto(@RequestParam Long nodeId,
                                              @RequestParam MultipartFile file) {
        return ResponseEntity.ok(photoService.add(nodeId, file));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<NodePhoto> updatePhoto(@RequestBody Map<String, String> req) {
        Long id = Long.valueOf(req.get("id"));
        String desc = req.get("description");
        return ResponseEntity.ok(photoService.updateDescription(id, desc));
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> deletePhoto(@RequestParam Long id) {
        photoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}