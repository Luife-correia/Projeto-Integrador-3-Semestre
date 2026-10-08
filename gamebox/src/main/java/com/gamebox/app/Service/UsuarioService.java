package com.gamebox.app.Service;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.RequestUserLogin;
import com.gamebox.app.Dto.response.UserLoginResponse;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository userRepository;

    public UsuarioService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }


    //Método auxiliar
    private Usuario buscarUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() ->
                        new RuntimeException
                                ("Usuario não encontrado!"));
    }

    //Método auxiliar
    private void vetificarEmail(Usuario usuario) {
        if (userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
    }

    //Método auxiliar
    private void verificarEmailAtualizacao(Usuario usuario) {
        Usuario usuarioExistente = buscarUser(usuario.getId());

        if (!usuarioExistente.getEmail().equals(usuario.getEmail())
                && userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
    }

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
        return buscarUser(id);
    }

    public Usuario atualizar(Usuario usuario) {
        buscarUser(usuario.getId());
        verificarEmailAtualizacao(usuario);
        return userRepository.save(usuario);
    }

    public UserLoginResponse login(RequestUserLogin request) {

        Usuario usuario = userRepository.findByEmailAndSenhaHash(
                request.email(),
                request.senha()
        ).orElseThrow(() -> new RuntimeException("Credenciais inválidas."));

        return new UserLoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
}




