package com.gamebox.app.Service;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Repository.AvaliacaoRepository;

public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository){
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao salvar(Avaliacao avaliacao){
        return avaliacaoRepository.save(avaliacao);
    }

    //criar avaliação
    //atualizar
    //deletar avaliação (user que criou)
    //listar avaliações de um jogo
    //deletar avaliação (admin)






}
