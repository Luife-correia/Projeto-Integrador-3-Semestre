package com.gamebox.app.Controller;

import com.gamebox.app.Domain.UsuarioJogo;
import com.gamebox.app.Dto.request.StatusUsuarioJogoRequest;
import com.gamebox.app.Dto.response.UsuarioJogoResponse;
import com.gamebox.app.Service.UsuarioJogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{usuarioId}/jogos/{jogoId}")
public class UsuarioJogoController {

    private final UsuarioJogoService usuarioJogoService;

    public UsuarioJogoController(UsuarioJogoService usuarioJogoService) {
        this.usuarioJogoService = usuarioJogoService;
    }

    // Adiciona o jogo à biblioteca do usuário.
    @PostMapping
    public ResponseEntity<UsuarioJogoResponse> criar(
            @PathVariable Long usuarioId,
            @PathVariable Long jogoId
    ) {
        UsuarioJogo usuarioJogo =
                usuarioJogoService.criar(usuarioId, jogoId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paraResposta(usuarioJogo));
    }

    // Busca o jogo na biblioteca do usuário.
    @GetMapping
    public ResponseEntity<UsuarioJogoResponse> buscar(
            @PathVariable Long usuarioId,
            @PathVariable Long jogoId
    ) {
        return ResponseEntity.ok(paraResposta(
                usuarioJogoService.buscar(usuarioId, jogoId)
        ));
    }

    // Atualiza o status do jogo para este usuário.
    @PatchMapping("/status")
    public ResponseEntity<UsuarioJogoResponse> atualizarStatus(
            @PathVariable Long usuarioId,
            @PathVariable Long jogoId,
            @RequestBody @Valid StatusUsuarioJogoRequest request
    ) {
        return ResponseEntity.ok(paraResposta(
                usuarioJogoService.atualizarStatus(
                        usuarioId,
                        jogoId,
                        request.status()
                )
        ));
    }

    private UsuarioJogoResponse paraResposta(UsuarioJogo usuarioJogo) {
        return new UsuarioJogoResponse(
                usuarioJogo.getId(),
                usuarioJogo.getUsuario().getId(),
                usuarioJogo.getJogo().getId(),
                usuarioJogo.getStatus()
        );
    }
}
