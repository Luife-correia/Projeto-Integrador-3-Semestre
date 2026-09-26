package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findByNomeContainingIgnoreCase(String nome);
}
