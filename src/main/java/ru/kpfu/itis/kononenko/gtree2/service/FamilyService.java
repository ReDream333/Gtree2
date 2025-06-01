package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.entity.Node;

public interface FamilyService {
    void addInitialNode(Node firstNode);          // «корневой» в дереве
    Node addNewNode(Node newMember, Long childId);
}