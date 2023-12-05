package com.example.bookstore.service.impl;

import com.example.bookstore.dao.repository.RoleRepository;
import com.example.bookstore.dao.repository.UserRepository;
import com.example.bookstore.dto.user.UpdateRoleDto;
import com.example.bookstore.dto.user.UpdateRoleResponseDto;
import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import com.example.bookstore.service.UserService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final ShoppingCartServiceImpl shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.ROLE_USER)));
        User savedUser = userRepository.save(user);
        shoppingCartService.registerShoppingCart(savedUser);
        return userMapper.toUserResponseDto(savedUser);
    }

    public UpdateRoleResponseDto addRole(Long userId, UpdateRoleDto updateRoleDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No user with id " + userId));

        Set<Role> usersRoleIds = user.getRoles();
        Set<Role> newUsersRoleIds =
                updateRoleDto.roleIds().stream()
                        .map(id -> roleRepository.findById(id).orElseThrow(
                                () -> new EntityNotFoundException("No role with id " + id)))
                        .collect(Collectors.toSet());

        Set<Role> allRoles = new HashSet<>(usersRoleIds);
        allRoles.addAll(newUsersRoleIds);
        user.setRoles(allRoles);
        return userMapper.toUserUpdateRolesDto(userRepository.save(user));
    }
}
