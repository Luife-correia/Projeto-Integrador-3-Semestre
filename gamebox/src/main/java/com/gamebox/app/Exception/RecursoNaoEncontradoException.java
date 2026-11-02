package com.gamebox.app.Exception;

// Indica que o recurso solicitado não existe no banco de dados.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
