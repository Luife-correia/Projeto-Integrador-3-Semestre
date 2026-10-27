package com.gamebox.app.Service;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Domain.UsuarioJogo;
import com.gamebox.app.Enum.StatusJogos;
import com.gamebox.app.Repository.AvaliacaoRepository;
import com.gamebox.app.Repository.JogoRepository;
import com.gamebox.app.Repository.UsuarioJogoRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioJogoService {

    private final UsuarioJogoRepository usuarioJogoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public UsuarioJogoService(
            UsuarioJogoRepository usuarioJogoRepository,
            UsuarioRepository usuarioRepository,
            JogoRepository jogoRepository,
            AvaliacaoRepository avaliacaoRepository
    ) {
        this.usuarioJogoRepository = usuarioJogoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoRepository = jogoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Transactional
    public UsuarioJogo criar(Long usuarioId, Long jogoId) {
        UsuarioJogo existente = usuarioJogoRepository
                .findByUsuarioIdAndJogoId(usuarioId, jogoId)
                .orElse(null);

        if (existente != null) {
            return existente;
        }

        Usuario usuario = buscarUsuario(usuarioId);
        Jogo jogo = buscarJogo(jogoId);

        return usuarioJogoRepository.save(new UsuarioJogo(usuario, jogo));
    }

    @Transactional(readOnly = true)
    public UsuarioJogo buscar(Long usuarioId, Long jogoId) {
        return usuarioJogoRepository
                .findByUsuarioIdAndJogoId(usuarioId, jogoId)
                .orElseThrow(() -> new RuntimeException(
                        "Jogo não encontrado na biblioteca do usuário."
                ));
    }

    @Transactional
    public UsuarioJogo atualizarStatus(
            Long usuarioId,
            Long jogoId,
            StatusJogos novoStatus
    ) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O status do jogo é obrigatório.");
        }

        UsuarioJogo usuarioJogo = usuarioJogoRepository
                .findByUsuarioIdAndJogoId(usuarioId, jogoId)
                .orElseGet(() -> criar(usuarioId, jogoId));

        if (usuarioJogo.getStatus() != StatusJogos.NAO_JOGADO
                && novoStatus == StatusJogos.NAO_JOGADO
                && avaliacaoRepository.existsByUsuarioIdAndJogoId(usuarioId, jogoId)) {
            throw new IllegalStateException(
                    "Não é possível voltar o jogo para NAO_JOGADO enquanto existir uma avaliação."
            );
        }

        usuarioJogo.setStatus(novoStatus);
        return usuarioJogoRepository.save(usuarioJogo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioJogo> listarPorStatus(
            Long usuarioId,
            StatusJogos status
    ) {
        if (status == null) {
            throw new IllegalArgumentException("O status é obrigatório.");
        }

        buscarUsuario(usuarioId);
        return usuarioJogoRepository.findByUsuarioIdAndStatus(usuarioId, status);
    }

    @Transactional(readOnly = true)
    public List<UsuarioJogo> listarBiblioteca(Long usuarioId) {
        buscarUsuario(usuarioId);

        // Jogos NAO_JOGADO ainda não entram na biblioteca principal.
        return usuarioJogoRepository.findByUsuarioIdAndStatusNot(
                usuarioId,
                StatusJogos.NAO_JOGADO
        );
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado com id: " + usuarioId
                ));
    }

    private Jogo buscarJogo(Long jogoId) {
        return jogoRepository.findById(jogoId)
                .orElseThrow(() -> new RuntimeException(
                        "Jogo não encontrado com id: " + jogoId
                ));
    }
}
