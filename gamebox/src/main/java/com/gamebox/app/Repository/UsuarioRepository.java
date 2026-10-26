package com.gamebox.app.Repository;

import com.gamebox.app.Domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositório responsável pelas operações de banco de dados dos usuários.
// O JpaRepository já fornece métodos como save, findById, findAll e deleteById.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica se já existe um usuário cadastrado com esse e-mail.
    boolean existsByEmail(String email);

    // Busca um usuário pelo e-mail.
    // O Optional permite representar o caso em que nenhum usuário foi encontrado.
    Optional<Usuario> findByEmail(String email);
}