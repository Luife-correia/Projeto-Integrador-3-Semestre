package com.gamebox.app.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAtualizacaoRequest(
        @NotBlank
        @Size(min = 5, message = "O nome deve ter pelo menos 5 caracteres")
        String nome,

        @NotBlank
        @Email
        String email,

        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        String fotoPerfil,
        String bio
) {
}
