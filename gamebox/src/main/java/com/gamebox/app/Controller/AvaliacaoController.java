
package com.gamebox.app.Controller;

import com.gamebox.app.Dto.request.AvaliacaoAtualizacaoRequest;
import com.gamebox.app.Dto.request.AvaliacaoRequest;
import com.gamebox.app.Dto.response.AvaliacaoResponse;
import com.gamebox.app.Service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    // Criar uma avaliação
    @PostMapping
    public ResponseEntity<AvaliacaoResponse> salvar(
            @RequestBody @Valid AvaliacaoRequest request
    ) {
        AvaliacaoResponse novaAvaliacao = avaliacaoService.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    // Buscar uma avaliação pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> buscar(@PathVariable Long id) {
        AvaliacaoResponse avaliacao =
                avaliacaoService.paraResposta(avaliacaoService.buscar(id));

        return ResponseEntity.ok(avaliacao);
    }

    // Buscar todas as avaliações de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AvaliacaoResponse>> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        List<AvaliacaoResponse> avaliacoes = avaliacaoService
                .buscarPorUsuario(usuarioId)
                .stream()
                .map(avaliacaoService::paraResposta)
                .toList();

        return ResponseEntity.ok(avaliacoes);
    }

    // Buscar todas as avaliações de um jogo
    @GetMapping("/jogo/{jogoId}")
    public ResponseEntity<List<AvaliacaoResponse>> buscarPorJogo(
            @PathVariable Long jogoId) {

        List<AvaliacaoResponse> avaliacoes = avaliacaoService
                .buscarPorJogo(jogoId)
                .stream()
                .map(avaliacaoService::paraResposta)
                .toList();

        return ResponseEntity.ok(avaliacoes);
    }

    // Atualizar uma avaliação
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AvaliacaoAtualizacaoRequest request) {

        AvaliacaoResponse avaliacao =
                avaliacaoService.atualizar(id, request);

        return ResponseEntity.ok(avaliacao);
    }

    // Deletar uma avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
