package com.gamebox.app.Controller;

import com.gamebox.app.Domain.BibliotecaJogo;
import com.gamebox.app.Service.BibliotecaJogoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bliblioteca")
public class BibliotecaJogoController {


    private final BibliotecaJogoService bibliotecaJogoService;

    public BibliotecaJogoController(BibliotecaJogoService bibliotecaJogoService) {
        this.bibliotecaJogoService = bibliotecaJogoService;
    }

    @PostMapping
    public ResponseEntity<BibliotecaJogo> salvar(

            @RequestBody @Valid BibliotecaJogo biblioteca
    ) {

        BibliotecaJogo resposta =
                bibliotecaJogoService.salvar(biblioteca);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        bibliotecaJogoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaJogo> buscarBibliotecaJogoPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                bibliotecaJogoService.buscarBibliotecaJogoPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BibliotecaJogo> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid BibliotecaJogo bibliotecaJogo
    ) {
        bibliotecaJogo.setId(id);
        BibliotecaJogo resposta =
                bibliotecaJogoService.atualizar(bibliotecaJogo);

        return ResponseEntity.ok(resposta);
    }

}

