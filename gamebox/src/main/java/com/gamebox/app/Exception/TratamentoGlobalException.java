package com.gamebox.app.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratamentoGlobalException {

    // Converte recursos ausentes em respostas HTTP 404.
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> recursoNaoEncontrado(
            RecursoNaoEncontradoException exception
    ) {
        return resposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    // Informa ao cliente que a operação viola uma regra do sistema.
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> regraDeNegocio(
            RegraNegocioException exception
    ) {
        return resposta(HttpStatus.CONFLICT, exception.getMessage());
    }

    // Trata parâmetros inválidos que não são capturados pela validação dos DTOs.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> argumentoInvalido(
            IllegalArgumentException exception
    ) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // Junta os erros de validação dos campos em uma única mensagem objetiva.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> argumentoNaoValidado(
            MethodArgumentNotValidException exception
    ) {
        String mensagem = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    // Trata violações de validação disparadas diretamente pelo Bean Validation.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> violacaoDeRestricao(
            ConstraintViolationException exception
    ) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // Trata JSON malformado, datas inválidas e valores incompatíveis com o DTO.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> corpoNaoLegivel(
            HttpMessageNotReadableException exception
    ) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido.");
    }

    // Mantém o formato das respostas de erro igual em todos os endpoints.
    private ResponseEntity<Map<String, Object>> resposta(
            HttpStatus status,
            String mensagem
    ) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", mensagem
        ));
    }
}
