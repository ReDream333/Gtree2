package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.entity.NodeBiography;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeBiographyRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeBiographyService;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeBiographyServiceImpl implements NodeBiographyService {

    private final NodeBiographyRepository biographyRepository;
    private final NodeRepository nodeRepository;

    @Override
    public String get(Long nodeId) {
        return biographyRepository.findByNodeId(nodeId)
                .map(NodeBiography::getBiography)
                .orElse("");
    }

    @Override
    public void save(Long nodeId, String biography) {
        nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found id=" + nodeId));
        NodeBiography entity = biographyRepository.findByNodeId(nodeId)
                .orElseGet(NodeBiography::new);
        entity.setNodeId(nodeId);
        entity.setBiography(biography);
        biographyRepository.save(entity);
    }

    @Override
    public void delete(Long nodeId) {
        biographyRepository.deleteByNodeId(nodeId);
    }
}