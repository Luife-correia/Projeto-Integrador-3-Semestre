package com.gamebox.app.Repository;

import com.gamebox.app.Domain.UsuarioJogo;
import com.gamebox.app.Enum.StatusJogos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJogoRepository extends JpaRepository<UsuarioJogo, Long> {

    // Garante que o usuário tenha apenas um status para cada jogo.
    Optional<UsuarioJogo> findByUsuarioIdAndJogoId(Long usuarioId, Long jogoId);

    // Permite separar a biblioteca por status.
    List<UsuarioJogo> findByUsuarioIdAndStatus(Long usuarioId, StatusJogos status);

    List<UsuarioJogo> findByUsuarioIdAndStatusNot(Long usuarioId, StatusJogos status);
}
