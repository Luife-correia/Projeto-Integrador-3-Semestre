package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.StatusListaJogos;

public record ListaJogosResponse(

        Long id,

        JogoResumoResponse jogo,

        String nome,

        Boolean publica,

        StatusListaJogos status
) {
}
