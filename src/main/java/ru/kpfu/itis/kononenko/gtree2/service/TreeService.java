package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeCreateRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;

import java.util.List;

public interface TreeService {

    long createTree(TreeCreateRequest request);

    List<Tree> getPublicTrees();

    boolean isUserOwnsTree(Long userId, Long treeId);

    String nodesAsJson(Long treeId);

    String relationsAsJson(Long treeId);

    List<Tree> getTreesByUserId(Long userId);
}