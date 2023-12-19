package com.example.bookstore.dto.user;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateRoleDto(
        @NotEmpty(message = "You should add at least one role id")
        Set<Long> roleIds
) {
}
