package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.StatusJogos;

// Combina os dados do catálogo com o status do jogo para o usuário.
public record BibliotecaJogoResponse(
        Long jogoId,
        String capa,
        String nome,
        String desenvolvedora,
        double notaTotal,
        StatusJogos status
) {
}
