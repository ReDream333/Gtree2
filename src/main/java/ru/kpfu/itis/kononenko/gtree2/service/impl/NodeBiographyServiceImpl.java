package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeBiographyService;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeBiographyServiceImpl implements NodeBiographyService {

    private final NodeRepository nodeRepository;

    @Override
    public String getBiography(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        return node.getComment();
    }

    @Override
    public void saveBiography(Long nodeId, String biography) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        node.setComment(biography);
        nodeRepository.save(node);
    }

    @Override
    public void deleteBiography(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        node.setComment(null);
        nodeRepository.save(node);
    }
}