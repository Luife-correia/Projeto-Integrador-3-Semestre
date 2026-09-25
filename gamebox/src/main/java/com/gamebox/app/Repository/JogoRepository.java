package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
}
