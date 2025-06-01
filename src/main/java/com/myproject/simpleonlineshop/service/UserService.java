package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.dto.CreateUserRequest;
import com.myproject.simpleonlineshop.dto.UpdateUserRequest;
import com.myproject.simpleonlineshop.model.User;

public interface UserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    User getAuthenticatedUser();
}
