package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodeService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepo;

    @Override
    public String getNodeName(Long nodeId) {
        return nodeRepo.findFullName(nodeId);
    }

    @Override
    public Node updateNode(Long id, String first, String last, LocalDate birth, LocalDate death, String photo) {
        Node node = nodeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Node not found"));
        node.setFirstName(first);
        node.setLastName(last);
        node.setBirthDate(birth);
        node.setDeathDate(death);
        node.setPhotoUrl(photo);
        return node;
    }

    @Override
    public void deleteNode(Long nodeId) {
        nodeRepo.deleteById(nodeId);
    }
}
