package com.gamebox.app.Service;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository userRepository;

    public UsuarioService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * ********** Métodos auxiliares **********
     */

    private Usuario buscarUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() ->
                        new RuntimeException
                                ("Usuario não encontrado!"));
    }

    /**
     * ********** CRUD **********
     */

    public Usuario salvar(Usuario usuario) {

        if (userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        return userRepository.save(usuario);
    }

    public void deletar(Long id){

        buscarUser(id);
         userRepository.deleteById(id);
    }

    public List<Usuario> listartodos(){
        return userRepository.findAll();
    }
}
