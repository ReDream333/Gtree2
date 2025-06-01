package ru.kpfu.itis.kononenko.gtree2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeCreateRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;
import ru.kpfu.itis.kononenko.gtree2.mapper.NodeMapper;
import ru.kpfu.itis.kononenko.gtree2.mapper.TreeMapper;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.ParentChildRelationRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.TreeService;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TreeServiceImpl implements TreeService {
    private final TreeRepository treeRepository;
    private final NodeRepository nodeRepository;
    private final ParentChildRelationRepository relationRepository;
    private final TreeMapper treeMapper;
    private final NodeMapper nodeMapper;
    private final ObjectMapper mapper = new ObjectMapper();

//    public String getNodesJson(Long treeId) {
//        List<NodeDto> nodes = nodeRepository.findByTreeId(treeId).stream()
//            .map(NodeDto::fromEntity)
//            .collect(Collectors.toList());
//        return new Gson().toJson(nodes);
//    }
//
//    public String getRelationsJson(Long treeId) {
//        // Логика получения связей между нодами
//    }

    @Override
    public long createTree(TreeCreateRequest request) {
        Tree tree = treeMapper.toTree(request);
        return treeRepository.save(tree).getId();
    }

    @Override
    public List<Tree> getTreesByUsername(String username) {
        return treeRepository.findByUsername(username);
    }

    @Override
    public List<Tree> getPublicTrees() {
        return treeRepository.findByIsPrivateFalse();
    }

    @Override
    public boolean isUserOwnsTree(Long userId, Long treeId) {
        return treeRepository.findById(treeId)
                .map(t -> t.getUserId().equals(userId))
                .orElse(false);
    }

    @Override
    public String nodesAsJson(Long treeId) {
        return write(nodeRepository.findByTreeId(treeId)
                .stream()
                .map(nodeMapper::toDto)
                .toList());
    }


    private String write(Object o) {
        try { return mapper.writeValueAsString(o);}
        catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }

    @Override
    public String relationsAsJson(Long treeId) {
        return write(relationRepository.findByTree(treeId)
                .stream()
                .map(r -> Map.of(
                        "from", r.getChild().getId(),
                        "to",   r.getParent().getId()))
                .toList());
    }
}