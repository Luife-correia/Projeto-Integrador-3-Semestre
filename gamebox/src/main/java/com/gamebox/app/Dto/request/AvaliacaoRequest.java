package com.gamebox.app.Dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequest(
        @NotNull Long usuarioId,
        @NotNull Long jogoId,
        @Min(value = 0, message = "A nota deve ser no mínimo 0")
        @Max(value = 10, message = "A nota deve ser no máximo 10")
        int notaEmMeiasEstrelas,
        @NotBlank
        @Size(min = 10, max = 500, message = "O comentário deve ter entre 10 e 500 caracteres")
        String comentario
) {
}
