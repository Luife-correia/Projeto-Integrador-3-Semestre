package com.gamebox.app.Controller;

import com.gamebox.app.Dto.request.JogoRequest;
import com.gamebox.app.Dto.response.JogoResponse;
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
    public JogoResponse criar(@RequestBody @Valid JogoRequest jogo) {
        return jogoService.salvar(jogo);
    }

    @GetMapping("/{id}")
    public JogoResponse buscar(@PathVariable Long id) {
        return jogoService.buscarResposta(id);
    }

    @GetMapping
    public List<JogoResponse> listarTodos(@RequestParam(required = false) String nome){
        return jogoService.listarTodos(nome);
    }


    @PutMapping("/{id}")
    public JogoResponse atualizar(@PathVariable Long id, @RequestBody @Valid JogoRequest jogo){
        return jogoService.atualizar(id,jogo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        jogoService.deletar(id);
    }

}
