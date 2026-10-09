package com.gamebox.app.Controller;


import com.gamebox.app.Dto.request.ListaJogosRequest;
import com.gamebox.app.Dto.response.ListaJogosResponse;
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
    public ResponseEntity<ListaJogosResponse> salvar(
            @RequestBody @Valid ListaJogosRequest request
    ) {
        ListaJogosResponse resposta =
                listaJogosService.criarLista(request);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        listaJogosService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/")
    public ResponseEntity<ListaJogosResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ListaJogosRequest request
    ) {
        ListaJogosResponse resposta =
                listaJogosService.atualizar(request,id);

        return ResponseEntity.ok(resposta);
    }

}
