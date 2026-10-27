package com.gamebox.app.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListaJogosRequest(
        @NotBlank(message = "O nome da lista é obrigatório")
        String nome,

        @NotNull(message = "A visibilidade da lista (pública/privada) é obrigatória")
        Boolean publica,

        boolean principal,

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId
) {}
