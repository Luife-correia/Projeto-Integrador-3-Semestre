package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao,Long> {

    //busca todas avaliações por user
    List<Avaliacao> findByUsuarioId(Long usuarioId);

    //busca todas avaliações por jogo
    List<Avaliacao> findByJogoId(Long jogoId);
}
