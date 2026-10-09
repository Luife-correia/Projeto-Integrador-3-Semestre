package com.gamebox.app.Service;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.UserAtualizacaoRequest;
import com.gamebox.app.Dto.request.UserCadastroRequest;
import com.gamebox.app.Dto.request.UserLoginRequest;
import com.gamebox.app.Dto.response.UserResponse;
import com.gamebox.app.Dto.response.UserLoginResponse;
import com.gamebox.app.Enum.Role;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public UserResponse salvar(UserCadastroRequest request) {

        Usuario usuario = new Usuario();

        usuario.setNome
                (request.nome());

        usuario.setEmail
                (request.email());

        usuario.setSenhaHash
                (request.senha());

        usuario.setFotoPerfil
                (request.fotoPerfil());

        usuario.setBio
                (request.bio());

        usuario.setRole
                (Role.USER);

        usuario.setDataCriacao
                (LocalDate.now());

        vetificarEmail(usuario);
        return toResponse(userRepository.save(usuario));
    }

    public void deletar(Long id) {

        buscarUser(id);
        userRepository.deleteById(id);
    }

    public List<UserResponse> listartodos() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse buscarUsuario(Long id) {
        return toResponse(buscarUser(id));
    }

    public UserResponse atualizar(Long id, UserAtualizacaoRequest request) {
        Usuario usuario = buscarUser(id);

        if (!usuario.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setFotoPerfil(request.fotoPerfil());
        usuario.setBio(request.bio());

        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenhaHash(request.senha());
        }

        return toResponse(userRepository.save(usuario));
    }

    public UserLoginResponse login(UserLoginRequest request) {

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

    private UserResponse toResponse(Usuario usuario) {
        return new UserResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoPerfil(),
                usuario.getBio(),
                usuario.getRole(),
                usuario.getDataCriacao()
        );
    }
}



