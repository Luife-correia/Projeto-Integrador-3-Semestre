package com.gamebox.app.Dto.request;

import com.gamebox.app.Enum.StatusListaJogos;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public record ListaJogosRequest(
        Long jogoId,

        @NotBlank
        String nome,

        @NotBlank
        Boolean publica,

        @Enumerated(EnumType.STRING)
        StatusListaJogos status
) {
}
