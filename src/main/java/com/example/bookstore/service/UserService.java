package com.example.bookstore.service;

import com.example.bookstore.dto.user.UpdateRoleDto;
import com.example.bookstore.dto.user.UpdateRoleResponseDto;
import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

    UpdateRoleResponseDto addRole(Long userId, UpdateRoleDto updateRoleDto);
}
