package ru.kpfu.itis.kononenko.gtree2.mapper;


import org.mapstruct.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.response.UserResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = "password", target = "passwordHash")
    User toUser(UserRegisterRequest request);

    UserResponse toResponse(User user);

    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User entity, UserRequest dto);
}