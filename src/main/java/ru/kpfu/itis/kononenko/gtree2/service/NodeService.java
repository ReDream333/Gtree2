package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.dto.response.NodeResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;


public interface NodeService {
    NodeResponse get(Long nodeId);
    NodeResponse save(Long treeId, NodeFormRequest form);           // если в form.childId == null → root
    NodeResponse update(Long nodeId, NodeFormRequest form);
    void         delete(Long nodeId);

}