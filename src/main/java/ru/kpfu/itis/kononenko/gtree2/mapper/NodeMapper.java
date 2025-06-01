package ru.kpfu.itis.kononenko.gtree2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.kpfu.itis.kononenko.gtree2.dto.NodeDto;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NodeMapper {
    NodeDto toDto(Node node);
    Node toEntity(NodeDto nodeDto);
}
