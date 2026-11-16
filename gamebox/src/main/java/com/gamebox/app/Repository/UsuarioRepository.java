package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    // O Optional permite representar o caso em que nenhum usuário foi encontrado.
    Optional<Usuario> findByEmail(String email);
}