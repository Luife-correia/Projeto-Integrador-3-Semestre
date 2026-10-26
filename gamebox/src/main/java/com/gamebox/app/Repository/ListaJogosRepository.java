package com.gamebox.app.Repository;

import com.gamebox.app.Domain.ListaJogos;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório responsável pelas operações de banco de dados das listas de jogos.
// O JpaRepository já disponibiliza métodos para salvar, buscar, atualizar e excluir registros.
public interface ListaJogosRepository extends JpaRepository<ListaJogos, Long> {
}