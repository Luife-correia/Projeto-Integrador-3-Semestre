package com.gamebox.app.Controller;

import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Service.ListaJogosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listas-jogos")
public class ListaJogosController {

    private final ListaJogosService listaJogosService;

    public ListaJogosController(ListaJogosService listaJogosService) {
        this.listaJogosService = listaJogosService;
    }


    @PostMapping
    public ResponseEntity<ListaJogos> salvar(
            @RequestBody @Valid ListaJogos listaJogos
    ) {
        ListaJogos resposta =
                listaJogosService.salvar(listaJogos);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        listaJogosService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ListaJogos> atualizar(
            @RequestBody @Valid ListaJogos listaJogos
    ) {
        ListaJogos resposta =
                listaJogosService.atualizar(listaJogos);

        return ResponseEntity.ok(resposta);
    }

}
