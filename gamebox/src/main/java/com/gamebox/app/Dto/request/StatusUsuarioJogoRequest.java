package com.gamebox.app.Dto.request;

import com.gamebox.app.Enum.StatusJogos;
import jakarta.validation.constraints.NotNull;

public record StatusUsuarioJogoRequest(
        @NotNull StatusJogos status
) {
}
