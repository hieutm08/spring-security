package com.pm.security.mapper;

import com.pm.security.dto.request.UserCreationRequest;
import com.pm.security.dto.request.UserUpdateRequest;
import com.pm.security.dto.response.UserResponse;
import com.pm.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdate);
}
