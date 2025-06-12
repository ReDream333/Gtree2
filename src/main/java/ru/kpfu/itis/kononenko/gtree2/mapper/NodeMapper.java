package ru.kpfu.itis.kononenko.gtree2.mapper;

import org.mapstruct.*;
import ru.kpfu.itis.kononenko.gtree2.dto.response.NodeResponse;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NodeMapper {
    @Mapping(target = "key",      source = "id")
    @Mapping(target = "fullName", expression = "java(node.getFirstName() + \" \" + node.getLastName())")
    @Mapping(target = "gender",   expression = "java(String.valueOf(node.getGender()))")
    @Mapping(target = "birthday", expression = "java(node.getBirthDate() != null ? node.getBirthDate().toString() : \"Неизвестно\")")
    @Mapping(target = "death",    expression = "java(node.getDeathDate() != null ? node.getDeathDate().toString() : \"\")")
    @Mapping(target = "comment",  source = "comment")
    @Mapping(target = "photo",    source = "photoUrl")
    @Mapping(target = "zodiacSign", source = "zodiacSign")
    NodeResponse toResponse(Node node);

    Node toEntity(NodeFormRequest nodeDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(NodeFormRequest form, @MappingTarget Node entity);
}

