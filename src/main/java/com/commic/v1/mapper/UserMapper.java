package com.commic.v1.mapper;

import com.commic.v1.dto.responses.UserResponse;
import com.commic.v1.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDTO(User user);
}
