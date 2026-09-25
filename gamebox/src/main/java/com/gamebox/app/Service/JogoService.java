package com.gamebox.app.Service;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Repository.JogoRepository;
import org.springframework.stereotype.Service;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public Jogo salvar(Jogo jogo) {
        return jogoRepository.save(jogo);
    }



}
