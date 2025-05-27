package com.myproject.simpleonlineshop.controller;

import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.CreateUserRequest;
import com.myproject.simpleonlineshop.dto.UpdateUserRequest;
import com.myproject.simpleonlineshop.dto.UserDto;
import com.myproject.simpleonlineshop.exception.AlreadyExistsException;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.mapper.MyModelMapper;
import com.myproject.simpleonlineshop.model.User;
import com.myproject.simpleonlineshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;
    private final MyModelMapper modelMapper;

    public UserController(UserService userService, MyModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = modelMapper.toUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = modelMapper.toUserDto(user);
            return ResponseEntity.ok(new ApiResponse("User Inserted Successfully !", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }


    }
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDto userDto = modelMapper.toUserDto(user);
            return ResponseEntity.ok(new ApiResponse("User Updated Successfully!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Deleted Successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
