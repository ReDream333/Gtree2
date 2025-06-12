package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.dto.response.NodeResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.ZodiacStatsResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;
import ru.kpfu.itis.kononenko.gtree2.mapper.NodeMapper;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeService;
import ru.kpfu.itis.kononenko.gtree2.service.NumberaService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static ru.kpfu.itis.kononenko.gtree2.utils.ZodiacUtils.getAdvice;
import static ru.kpfu.itis.kononenko.gtree2.utils.ZodiacUtils.getZodiacSign;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;
    private final TreeRepository treeRepository;
    private final NodeMapper nodeMapper;
    private final NumberaService numberaService;


    @Override
    public NodeResponse get(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found with id=%s".formatted(nodeId)));
        return nodeMapper.toResponse(node);
    }

    @Override
    public NodeResponse save(Long treeId, NodeFormRequest form) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new IllegalArgumentException("Tree not found with id=%s".formatted(treeId)));

        Node node = nodeMapper.toEntity(form);
        node.setTree(tree);

        if ((node.getComment() == null || node.getComment().isBlank()) && node.getBirthDate() != null) {
            String fact = numberaService.getFact(node.getBirthDate());
            node.setComment(fact);
        }

        node.setZodiacSign(getZodiacSign(node.getBirthDate()));
        Node saved = nodeRepository.save(node);

        Long childId = form.childId();

        if (childId != null) {
            Node child = nodeRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("Child not found id=" + childId));
            saved.setChild(child);
            saved = nodeRepository.save(saved);
        }

        return nodeMapper.toResponse(saved);
    }

    @Override
    public NodeResponse update(Long nodeId, NodeFormRequest form) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new IllegalArgumentException("Node not found: " + nodeId));
        nodeMapper.updateFromRequest(form, node);
        node.setZodiacSign(getZodiacSign(node.getBirthDate()));
        return nodeMapper.toResponse(node);
    }

    @Override
    public void delete(Long nodeId) {
        nodeRepository.deleteById(nodeId);
    }

    @Override
    @Transactional(readOnly = true)
    public ZodiacStatsResponse getZodiacStats(Long treeId) {
        List<Object[]> raw = nodeRepository.countZodiacSignsByTreeId(treeId);
        Map<String, Long> stats = raw.stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> (Long) r[1]));

        String dominant = stats.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        String message = getAdvice(dominant);

        return new ZodiacStatsResponse(stats, message);
    }


}
