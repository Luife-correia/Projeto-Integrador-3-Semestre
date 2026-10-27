package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.StatusJogos;

public record UsuarioJogoResponse(
        Long id,
        Long usuarioId,
        Long jogoId,
        StatusJogos status
) {
}
