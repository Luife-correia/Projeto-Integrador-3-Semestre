package com.gamebox.app.Dto.response;

import java.time.LocalDate;
import java.util.List;

public record JogoResponse(
        Long id,
        String nome,
        String capa,
        List<String> plataformas,
        List<String> generos,
        String desenvolvedora,
        LocalDate dataLancamento,
        String sinopse,
        double notaTotal
) {
}
