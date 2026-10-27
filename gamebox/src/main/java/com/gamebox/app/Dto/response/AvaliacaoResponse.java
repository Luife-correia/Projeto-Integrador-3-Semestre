package com.gamebox.app.Dto.response;

import java.time.LocalDate;

public record AvaliacaoResponse(
        Long id,
        Long usuarioId,
        Long jogoId,
        int notaEmMeiasEstrelas,
        String comentario,
        LocalDate dataCriacao
) {
}
