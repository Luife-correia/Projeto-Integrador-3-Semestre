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

    public void vetificarEmail(Usuario usuario) {
        if (userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
    }

    private void verificarEmailAtualizacao(Usuario usuario) {
        Usuario usuarioExistente = buscarUser(usuario.getId());

        if (!usuarioExistente.getEmail().equals(usuario.getEmail())
                && userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
    }

    /**
     * ********** CRUD **********
     */

    public Usuario salvar(Usuario usuario) {

        vetificarEmail(usuario);
        return userRepository.save(usuario);
    }

    public void deletar(Long id) {

        buscarUser(id);
        userRepository.deleteById(id);
    }

    public List<Usuario> listartodos() {
        return userRepository.findAll();
    }

    public Usuario buscarUsuario(Long id) {
        buscarUser(id);
        return buscarUser(id);
    }

    public Usuario atualizar(Usuario usuario) {
        buscarUser(usuario.getId());
        verificarEmailAtualizacao(usuario);
        return userRepository.save(usuario);
    }

    // ========= FALTA O METODO DE ATIVAR OU DESATIVAR A CONTA =========
}
