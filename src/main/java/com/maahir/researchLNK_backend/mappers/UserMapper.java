package com.maahir.researchLNK_backend.mappers;

import com.maahir.researchLNK_backend.dtos.auth.UserResponse;
import com.maahir.researchLNK_backend.persistence.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
