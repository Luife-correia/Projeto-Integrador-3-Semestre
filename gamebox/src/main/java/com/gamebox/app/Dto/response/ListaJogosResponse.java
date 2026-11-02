package com.gamebox.app.Dto.response;

import java.util.List;

public record ListaJogosResponse(
        Long id,
        String nome,
        Boolean publica,
        boolean principal,
        Long usuarioId,
        String usuarioNome,
        List<JogoResponse> jogos
) {}
