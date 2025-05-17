package ru.kpfu.itis.kononenko.gtree2.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = "password", target = "passwordHash")
    User toEntity(UserRegisterRequest userRegisterRequest);
}
