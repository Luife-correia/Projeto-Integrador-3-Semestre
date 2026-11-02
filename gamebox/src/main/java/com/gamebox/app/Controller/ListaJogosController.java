package com.gamebox.app.Controller;

import com.gamebox.app.Dto.request.ListaJogosRequest;
import com.gamebox.app.Dto.response.ListaJogosResponse;
import com.gamebox.app.Service.ListaJogosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ListaJogos")
public class ListaJogosController {

    private final ListaJogosService listaJogosService;

    public ListaJogosController(ListaJogosService listaJogosService) {
        this.listaJogosService = listaJogosService;
    }

    @PostMapping
    public ResponseEntity<ListaJogosResponse> salvar(
            @RequestBody @Valid ListaJogosRequest request
    ) {
        ListaJogosResponse resposta = listaJogosService.criarLista(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaJogosResponse> buscarPorId(@PathVariable Long id) {
        ListaJogosResponse resposta = listaJogosService.buscarResposta(id);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ListaJogosResponse>> listarTodas() {
        List<ListaJogosResponse> respostas = listaJogosService.listarTodas();
        return ResponseEntity.ok(respostas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ListaJogosResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ListaJogosResponse> respostas = listaJogosService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(respostas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaJogosResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ListaJogosRequest request
    ) {
        ListaJogosResponse resposta = listaJogosService.atualizar(id, request);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        listaJogosService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
