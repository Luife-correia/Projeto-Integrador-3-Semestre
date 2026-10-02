package com.gamebox.app.Repository;

import com.gamebox.app.Domain.ListaJogos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaJogosRepository extends JpaRepository<ListaJogos, Long> {
}
