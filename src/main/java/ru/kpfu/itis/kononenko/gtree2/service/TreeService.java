package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeCreateRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;

import java.util.List;

public interface TreeService {
    //TODO пусть принимает весь request
    long createTree(TreeCreateRequest request);

    List<Tree> getTreesByUsername(String username);

    List<Tree> getPublicTrees();

    boolean isUserOwnsTree(Long userId, Long treeId);


    //TODO это должна быть конвертация
    String nodesAsJson(Long treeId);
    String relationsAsJson(Long treeId);
}