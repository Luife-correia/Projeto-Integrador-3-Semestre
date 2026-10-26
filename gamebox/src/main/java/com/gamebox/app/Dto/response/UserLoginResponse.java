package com.gamebox.app.Dto.response;

public record UserLoginResponse

        (
                Long id,
                String nome,
                String email

        ) {
}
