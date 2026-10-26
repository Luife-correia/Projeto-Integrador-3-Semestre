
package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Service.AvaliacaoService;
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
    public ResponseEntity<Avaliacao> salvar(@RequestBody Avaliacao avaliacao) {
        Avaliacao novaAvaliacao = avaliacaoService.salvar(avaliacao);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    // Buscar uma avaliação pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscar(@PathVariable Long id) {
        Avaliacao avaliacao = avaliacaoService.buscar(id);

        return ResponseEntity.ok(avaliacao);
    }

    // Buscar todas as avaliações de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Avaliacao>> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        List<Avaliacao> avaliacoes =
                avaliacaoService.buscarPorUsuario(usuarioId);

        return ResponseEntity.ok(avaliacoes);
    }

    // Buscar todas as avaliações de um jogo
    @GetMapping("/jogo/{jogoId}")
    public ResponseEntity<List<Avaliacao>> buscarPorJogo(
            @PathVariable Long jogoId) {

        List<Avaliacao> avaliacoes =
                avaliacaoService.buscarPorJogo(jogoId);

        return ResponseEntity.ok(avaliacoes);
    }

    // Atualizar uma avaliação
    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(
            @PathVariable Long id,
            @RequestBody Avaliacao avaliacaoAtualizada) {

        Avaliacao avaliacao =
                avaliacaoService.atualizar(id, avaliacaoAtualizada);

        return ResponseEntity.ok(avaliacao);
    }

    // Deletar uma avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
