package com.gamebox.app.Controller;

import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Service.ListaJogosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ListaJogos")
public class ListaJogosController {

    private final ListaJogosService listaJogosService;

    public ListaJogosController(ListaJogosService listaJogosService) {
        this.listaJogosService = listaJogosService;
    }

    @PostMapping
    public ResponseEntity<ListaJogos> salvar(

            @RequestBody @Valid ListaJogos listaJogos
    ){

       var resposta =
               listaJogosService.criarLista(listaJogos);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        listaJogosService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaJogos> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ListaJogos listaJogos
    ) {
        ListaJogos resposta =
                listaJogosService.atualizar(listaJogos, id);

        return ResponseEntity.ok(resposta);
    }
}
