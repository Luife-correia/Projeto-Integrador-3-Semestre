package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.Role;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String nome,
        String email,
        String fotoPerfil,
        String bio,
        Role role,
        LocalDate dataCriacao
) {
}
