package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Service.JogoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    public Jogo criar(@RequestBody @Valid Jogo jogo) {
        return jogoService.salvar(jogo);
    }

    @GetMapping("/{id}")
    public Jogo buscar(@PathVariable Long id) {
        return jogoService.buscarJogo(id);
    }

    @GetMapping
    public List<Jogo> listarTodos(@RequestParam(required = false) String nome){
        return jogoService.listarTodos(nome);
    }


    @PutMapping("/{id}")
    public Jogo atualizar(@PathVariable Long id, @RequestBody @Valid Jogo jogo){
        return jogoService.atualizar(id,jogo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        jogoService.deletar(id);
    }

}
