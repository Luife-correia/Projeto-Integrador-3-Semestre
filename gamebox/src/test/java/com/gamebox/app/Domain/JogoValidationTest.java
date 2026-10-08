package com.gamebox.app.Domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JogoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarNotaNoIntervaloPermitido() {
        Jogo jogo = new Jogo(
                "Jogo Teste",
                "capa.png",
                List.of("PC"),
                List.of("Ação"),
                "Dev",
                LocalDate.now(),
                "Sinopse",
                7.5
        );

        assertTrue(validator.validate(jogo).isEmpty());
    }

    @Test
    void deveRejeitarNotaForaDoIntervaloPermitido() {
        Jogo jogoComNotaBaixa = new Jogo(
                "Jogo Teste",
                "capa.png",
                List.of("PC"),
                List.of("Ação"),
                "Dev",
                LocalDate.now(),
                "Sinopse",
                -0.1
        );

        Jogo jogoComNotaAlta = new Jogo(
                "Jogo Teste",
                "capa.png",
                List.of("PC"),
                List.of("Ação"),
                "Dev",
                LocalDate.now(),
                "Sinopse",
                10.1
        );

        assertFalse(validator.validate(jogoComNotaBaixa).isEmpty());
        assertFalse(validator.validate(jogoComNotaAlta).isEmpty());
    }
}
