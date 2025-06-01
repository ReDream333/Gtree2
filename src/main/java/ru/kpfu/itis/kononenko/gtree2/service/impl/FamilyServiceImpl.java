package ru.kpfu.itis.kononenko.gtree2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.entity.ParentChildRelation;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.ParentChildRelationRepository;
import ru.kpfu.itis.kononenko.gtree2.service.FamilyService;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final NodeRepository nodeRepo;
    private final ParentChildRelationRepository relRepo;

    @Override
    public void addInitialNode(Node firstNode) {
        nodeRepo.save(firstNode);
    }

    @Override
    public Node addNewNode(Node newMember, Long childId) {
        Node savedParent = nodeRepo.save(newMember);
        Node child       = nodeRepo.findById(childId)
                                   .orElseThrow(() -> new EntityNotFoundException("Child not found"));

        relRepo.save(ParentChildRelation.builder()
                                        .parent(savedParent)
                                        .child(child)
                                        .build());
        return savedParent;
    }
}
