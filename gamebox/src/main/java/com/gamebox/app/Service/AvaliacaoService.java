package com.gamebox.app.Service;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Domain.UsuarioJogo;
import com.gamebox.app.Dto.request.AvaliacaoAtualizacaoRequest;
import com.gamebox.app.Dto.request.AvaliacaoRequest;
import com.gamebox.app.Dto.response.AvaliacaoResponse;
import com.gamebox.app.Enum.StatusJogos;
import com.gamebox.app.Repository.AvaliacaoRepository;
import com.gamebox.app.Repository.JogoRepository;
import com.gamebox.app.Repository.UsuarioJogoRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;
    private final UsuarioJogoRepository usuarioJogoRepository;

    public AvaliacaoService(
            AvaliacaoRepository avaliacaoRepository,
            UsuarioRepository usuarioRepository,
            JogoRepository jogoRepository,
            UsuarioJogoRepository usuarioJogoRepository
    ) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoRepository = jogoRepository;
        this.usuarioJogoRepository = usuarioJogoRepository;
    }

    @Transactional
    public AvaliacaoResponse salvar(AvaliacaoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        Jogo jogo = jogoRepository.findById(request.jogoId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado."));

        if (avaliacaoRepository.existsByUsuarioIdAndJogoId(
                usuario.getId(),
                jogo.getId()
        )) {
            throw new IllegalStateException(
                    "O usuário já avaliou este jogo."
            );
        }

        validarStatusParaAvaliar(usuario.getId(), jogo.getId());

        Avaliacao avaliacao = new Avaliacao(
                usuario,
                jogo,
                request.notaEmMeiasEstrelas(),
                request.comentario(),
                null
        );

        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        recalcularNota(jogo);
        return paraResposta(salva);
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

    @Transactional
    public AvaliacaoResponse atualizar(Long id, AvaliacaoAtualizacaoRequest request) {
        Avaliacao avaliacaoAtual = buscar(id);

        validarStatusParaAvaliar(
                avaliacaoAtual.getUsuario().getId(),
                avaliacaoAtual.getJogo().getId()
        );

        avaliacaoAtual.setComentario(request.comentario());
        avaliacaoAtual.setNotaEmMeiasEstrelas(request.notaEmMeiasEstrelas());

        Avaliacao salva = avaliacaoRepository.save(avaliacaoAtual);
        recalcularNota(avaliacaoAtual.getJogo());
        return paraResposta(salva);
    }

    @Transactional
    public void deletar(Long id) {
        Avaliacao avaliacao = buscar(id);
        Jogo jogo = avaliacao.getJogo();
        avaliacaoRepository.deleteById(id);
        recalcularNota(jogo);
    }

    public AvaliacaoResponse paraResposta(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getUsuario().getId(),
                avaliacao.getJogo().getId(),
                avaliacao.getNotaEmMeiasEstrelas(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao()
        );
    }

    private void validarStatusParaAvaliar(Long usuarioId, Long jogoId) {
        UsuarioJogo usuarioJogo = usuarioJogoRepository
                .findByUsuarioIdAndJogoId(usuarioId, jogoId)
                .orElseThrow(() -> new IllegalStateException(
                        "O usuário precisa adicionar o jogo à biblioteca antes de avaliá-lo."
                ));

        if (usuarioJogo.getStatus() == StatusJogos.NAO_JOGADO) {
            throw new IllegalStateException(
                    "Só é possível avaliar um jogo que já foi jogado."
            );
        }
    }

    private void recalcularNota(Jogo jogo) {
        // Busca os valores atuais e atualiza a média do jogo.
        long somaTotalAvaliacoes = avaliacaoRepository
                .somarNotasPorJogo(jogo.getId());
        long totalAvaliacoes = avaliacaoRepository
                .countByJogoId(jogo.getId());

        jogo.atualizarNota(somaTotalAvaliacoes, totalAvaliacoes);
        jogoRepository.save(jogo);
    }

}
