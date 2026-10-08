package com.gamebox.app.Domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JogoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldRejectNotaGreaterThanFive() {
        Jogo jogo = new Jogo(
                "Jogo Teste",
                "https://example.com/capa.png",
                List.of("PC"),
                List.of("Ação"),
                "Dev Studio",
                LocalDate.now(),
                "Sinopse teste",
                5.1
        );

        Set<ConstraintViolation<Jogo>> violations = validator.validate(jogo);

        assertTrue(violations.stream().anyMatch(v -> "nota".equals(v.getPropertyPath().toString())));
    }

    @Test
    void shouldAcceptNotaWithinRange() {
        Jogo jogo = new Jogo(
                "Jogo Teste",
                "https://example.com/capa.png",
                List.of("PC"),
                List.of("Ação"),
                "Dev Studio",
                LocalDate.now(),
                "Sinopse teste",
                4.5
        );

        Set<ConstraintViolation<Jogo>> violations = validator.validate(jogo);

        assertFalse(violations.stream().anyMatch(v -> "nota".equals(v.getPropertyPath().toString())));
    }
}
