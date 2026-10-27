package com.gamebox.app.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record JogoRequest(
        @NotBlank String nome,
        @NotBlank String capa,
        @NotEmpty List<String> plataformas,
        @NotEmpty List<String> generos,
        @NotBlank String desenvolvedora,
        @NotNull LocalDate dataLancamento,
        @NotBlank String sinopse
) {
}
