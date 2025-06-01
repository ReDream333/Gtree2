package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.entity.Node;

import java.time.LocalDate;

public interface NodeService {

    String getNodeName(Long nodeId);

    //TODO пусть принимает весь request
    Node updateNode(Long id,
                    String first,
                    String last,
                    LocalDate birth,
                    LocalDate death,
                    String photo);

    void deleteNode(Long nodeId);
}