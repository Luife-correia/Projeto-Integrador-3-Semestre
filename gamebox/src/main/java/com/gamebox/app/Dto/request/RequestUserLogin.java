package com.gamebox.app.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUserLogin(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha
) {
}
