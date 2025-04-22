package com.pm.security.service;

import com.pm.security.dto.request.UserCreationRequest;
import com.pm.security.dto.request.UserUpdateRequest;
import com.pm.security.dto.response.UserResponse;
import com.pm.security.entity.Role;
import com.pm.security.entity.User;
import com.pm.security.exception.AppException;
import com.pm.security.exception.ErrorCode;
import com.pm.security.mapper.UserMapper;
import com.pm.security.repository.RoleRepository;
import com.pm.security.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User userEntity = userMapper.toUser(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(userEntity);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('APPROVE_DATA')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserByUserId(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest userUpdate) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found"));;
        userMapper.updateUser(user, userUpdate);
        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));

        var role = roleRepository.findAllById(userUpdate.getRoles());
        user.setRoles(new  HashSet<>(role));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();

        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);

    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
