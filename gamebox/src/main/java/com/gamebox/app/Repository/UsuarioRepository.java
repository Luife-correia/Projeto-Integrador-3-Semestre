package com.gamebox.app.Repository;


import com.gamebox.app.Domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    boolean existsByEmail( String email);

}
