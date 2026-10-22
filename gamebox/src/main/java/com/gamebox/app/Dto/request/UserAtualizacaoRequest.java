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
        @NotBlank
        String senha,

        String fotoPerfil,

        @Size(min = 6, message = "A bio deve ter pelo menos 6 caracteres")
        @Size(max = 100, message = "A bio deve ter no maximo 100 caracteres")
        String bio

) {
}
