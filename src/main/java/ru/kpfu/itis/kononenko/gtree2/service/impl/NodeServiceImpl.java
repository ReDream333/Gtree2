package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.dto.response.NodeResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;
import ru.kpfu.itis.kononenko.gtree2.mapper.NodeMapper;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeService;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;
    private final TreeRepository treeRepository;
    private final NodeMapper nodeMapper;

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
        return nodeMapper.toResponse(node);
    }

    @Override
    public void delete(Long nodeId) {
        nodeRepository.deleteById(nodeId);
    }


}
