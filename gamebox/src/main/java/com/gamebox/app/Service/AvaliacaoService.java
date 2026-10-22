package com.gamebox.app.Service;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository){
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao salvar(Avaliacao avaliacao){
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscar(Long id){
        return avaliacaoRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    public Avaliacao atualizar(Long id, Avaliacao avaliacaoAtualizada){
        Avaliacao avaliacaoAtual = buscar(id);

        avaliacaoAtual.setComentario(avaliacaoAtualizada.getComentario());
        avaliacaoAtual.setNotaEmMeiasEstrelas(avaliacaoAtualizada.getNotaEmMeiasEstrelas());

        return avaliacaoRepository.save(avaliacaoAtual);
    }

    public void deletar(Long id){
        buscar(id);
        avaliacaoRepository.deleteById(id);
    }

    //atualizar  ok
    //deletar avaliação (user que criou)
    //listar avaliações de um jogo
    //deletar avaliação (admin)

}
