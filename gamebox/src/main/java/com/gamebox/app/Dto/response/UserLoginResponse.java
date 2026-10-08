package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.Role;

public record UserLoginResponse

        (
                Long id,
                String nome,
                String email,
                Role role
        ) {
}
