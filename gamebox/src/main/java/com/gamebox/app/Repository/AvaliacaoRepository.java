package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao,Long> {

    //busca todas avaliações por user
    List<Avaliacao> findByUsuarioId(Long usuarioId);

    //busca todas avaliações por jogo
    List<Avaliacao> findByJogoId(Long jogoId);

    // Verifica se o usuário já avaliou este jogo.
    boolean existsByUsuarioIdAndJogoId(Long usuarioId, Long jogoId);

    // Soma todas as notas do jogo.
    @Query("SELECT COALESCE(SUM(a.notaEmMeiasEstrelas), 0) FROM Avaliacao a WHERE a.jogo.id = :jogoId")
    Long somarNotasPorJogo(@Param("jogoId") Long jogoId);

    // Conta quantas avaliações o jogo possui.
    long countByJogoId(Long jogoId);
}
