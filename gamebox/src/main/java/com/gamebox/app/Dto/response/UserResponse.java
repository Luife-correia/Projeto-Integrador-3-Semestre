package com.gamebox.app.Dto.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String nome,
        String email,
        String fotoPerfil,
        String bio,
        LocalDate dataCriacao
) {
}
