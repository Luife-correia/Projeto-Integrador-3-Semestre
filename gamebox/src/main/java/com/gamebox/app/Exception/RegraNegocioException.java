package com.gamebox.app.Exception;

// Representa uma operação válida tecnicamente, mas proibida pela regra de negócio.
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
        super(message);
    }
}
