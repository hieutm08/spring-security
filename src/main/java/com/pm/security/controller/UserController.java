package com.pm.security.controller;

import com.pm.security.dto.request.ApiResponse;
import com.pm.security.dto.request.UserCreationRequest;
import com.pm.security.dto.request.UserUpdateRequest;
import com.pm.security.dto.response.UserResponse;
import com.pm.security.entity.User;
import com.pm.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest user) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Create user successful");
        response.setResult(userService.createUser(user));
        return response;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>>getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> {
            log.info("GrantedAuthority : {}", grantedAuthority);
        });

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId) {
        return userService.getUserByUserId(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdate) {
        return userService.updateUser(userId, userUpdate);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @GetMapping("/my-info")
    public UserResponse myInfo(){
        return userService.getMyInfo();
    }
}
