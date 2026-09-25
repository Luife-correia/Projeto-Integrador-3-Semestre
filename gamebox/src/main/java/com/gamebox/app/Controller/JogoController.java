package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Service.JogoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    public Jogo criar(@RequestBody Jogo jogo) {
        return jogoService.salvar(jogo);
    }
}
