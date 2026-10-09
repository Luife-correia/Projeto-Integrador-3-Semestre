package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.RequestUserAtualizacao;
import com.gamebox.app.Dto.request.RequestUserCadastro;
import com.gamebox.app.Dto.request.RequestUserLogin;
import com.gamebox.app.Dto.response.UserResponse;
import com.gamebox.app.Dto.response.UserLoginResponse;
import com.gamebox.app.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<UserResponse> salvar(
            @RequestBody @Valid RequestUserCadastro request
    ) {

        UserResponse resposta =
                usuarioService.salvar(request);

        return ResponseEntity.ok(resposta);
    }

    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listartodos());
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuario(id));
    }

    // @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/Me")
    public ResponseEntity<UserResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid RequestUserAtualizacao request
    ) {
        UserResponse resposta =
                usuarioService.atualizar(id, request);
        return ResponseEntity.ok(resposta);
    }

    // falta inplemantar a segurança
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @RequestBody @Valid RequestUserLogin usuario
    ) {
        UserLoginResponse
                resposta =
                usuarioService.login(usuario);

        return ResponseEntity.ok(resposta);
    }
}
