package com.gamebox.app.Service;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.UserAtualizacaoRequest;
import com.gamebox.app.Dto.request.UserCadastroRequest;
import com.gamebox.app.Dto.request.UserLoginRequest;
import com.gamebox.app.Dto.response.UserResponse;
import com.gamebox.app.Dto.response.UserLoginResponse;
import com.gamebox.app.Enum.Role;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Método auxiliar
    private Usuario buscarUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() ->
                        new RuntimeException
                                ("Usuario não encontrado!"));
    }


    //Método auxiliar
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

    public UserResponse salvar(UserCadastroRequest request) {

        Usuario usuario = new Usuario();

        usuario.setNome
                (request.nome());

        usuario.setEmail
                (request.email());

        // Criando a senha a deixando ela oculta
        usuario.setSenhaHash
                (passwordEncoder.encode(request.senha()));

        usuario.setFotoPerfil
                (request.fotoPerfil());

        usuario.setBio
                (request.bio());

        usuario.setRole
                (Role.USER);

        return toResponse(
                userRepository.save(usuario)
        );
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

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setFotoPerfil(request.fotoPerfil());
        usuario.setBio(request.bio());

        return toResponse(userRepository.save(usuario));
    }

    public UserLoginResponse login(UserLoginRequest request) {

         Usuario usuario = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Credenciais inválidas."));


        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenhaHash())) {

            throw new RuntimeException
                    ("Credenciais inválidas.");
        }

        return new UserLoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

}



