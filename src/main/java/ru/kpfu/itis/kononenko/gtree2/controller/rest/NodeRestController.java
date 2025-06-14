package ru.kpfu.itis.kononenko.gtree2.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.response.CompatibilityResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.response.NodeResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.ZodiacStatsResponse;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.Map;

import ru.kpfu.itis.kononenko.gtree2.service.NodeService;
import ru.kpfu.itis.kononenko.gtree2.utils.ZodiacCompatibility;


import static ru.kpfu.itis.kononenko.gtree2.utils.ZodiacUtils.getZodiacSign;

@RestController
@RequestMapping("api/trees/{treeId}/nodes")
@RequiredArgsConstructor
public class NodeRestController {


    private final NodeService nodeService;


    /** Получить один узел */
    @GetMapping("/{nodeId}")
    public ResponseEntity<NodeResponse> get(
            @PathVariable Long nodeId) {

        return ResponseEntity.ok(
                        nodeService.get(nodeId)
        );
    }

    /** Создать новый узел */
    @PostMapping
    public ResponseEntity<NodeResponse> create(
            @PathVariable Long treeId,
            @RequestBody NodeFormRequest form
    ) {
        return ResponseEntity.ok(
                nodeService.save(treeId, form)
        );
    }

    /** Обновить существующий узел */
    @PutMapping("/{nodeId}")
    public ResponseEntity<NodeResponse> update(
            @PathVariable Long nodeId,
            @RequestBody NodeFormRequest form
    ) {
        return ResponseEntity.ok(
                nodeService.update(nodeId, form)
        );
    }

    /** Удалить узел */
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteNode(
            @PathVariable Long nodeId) {
        nodeService.delete(nodeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/zodiac")
    public ResponseEntity<String> getZodiac(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(getZodiacSign(date));
    }

    @GetMapping("/zodiac-stat")
    public ResponseEntity<ZodiacStatsResponse> zodiacStat(@PathVariable Long treeId) {
        return ResponseEntity.ok(nodeService.getZodiacStats(treeId));
    }

    @GetMapping("/compatibility")
    public ResponseEntity<CompatibilityResponse> compatibility(@RequestParam Long first,
                                                               @RequestParam Long second) {
        String sign1 = nodeService.get(first).zodiacSign();
        String sign2 = nodeService.get(second).zodiacSign();
        Integer percent = ZodiacCompatibility.computeCompatibility(sign1, sign2);
        return ResponseEntity.ok(CompatibilityResponse.builder().percent(percent).build());
    }

    @PostMapping("/{nodeId}/saveNodePhoto")
    public ResponseEntity<NodeResponse> saveNodePhoto(@PathVariable Long nodeId,
                                                      @RequestBody Map<String, String> body) {
        String photoUrl = body.get("imageUrl");
        return ResponseEntity.ok(nodeService.updatePhoto(nodeId, photoUrl));
    }


}
