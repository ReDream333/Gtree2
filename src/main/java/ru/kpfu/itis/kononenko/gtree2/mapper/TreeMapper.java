package ru.kpfu.itis.kononenko.gtree2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeCreateRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.TreeResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TreeMapper {

    Tree toTree(TreeCreateRequest request);

    TreeResponse toResponse(Tree tree);
}