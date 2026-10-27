package com.gamebox.app.Repository;

import com.gamebox.app.Domain.ListaJogos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositório responsável pelas operações de banco de dados das listas de jogos.
public interface ListaJogosRepository extends JpaRepository<ListaJogos, Long> {
    List<ListaJogos> findByUsuarioId(Long usuarioId);
    List<ListaJogos> findByPublicaTrue();
    List<ListaJogos> findByUsuarioIdAndPublicaTrue(Long usuarioId);
}
