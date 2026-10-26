package com.gamebox.app.Service;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository){
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao salvar(Avaliacao avaliacao){
        return avaliacaoRepository.save(avaliacao);
    }

    //metodo auxilar
    public Avaliacao buscar(Long id){
        return avaliacaoRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    // Busca todas as avaliações de um usuário
    public List<Avaliacao> buscarPorUsuario(Long usuarioId) {
        return avaliacaoRepository.findByUsuarioId(usuarioId);
    }

    // Busca todas as avaliações de um jogo
    public List<Avaliacao> buscarPorJogo(Long jogoId) {
        return avaliacaoRepository.findByJogoId(jogoId);
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

    //metodo delete pronto mas falta verificação por role e user
    //deletar avaliação (user que criou)
    //deletar avaliação (admin)

}
