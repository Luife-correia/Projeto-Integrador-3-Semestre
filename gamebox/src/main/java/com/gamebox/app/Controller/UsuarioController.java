package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {


    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<Usuario> salvar(
            @RequestBody @Valid Usuario usuario
    ) {

        Usuario resposta =
                usuarioService.salvar(usuario);

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
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listartodos());
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuario(id));
    }

    // @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/Me")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid
            Usuario usuario
    ) {
        usuario.setId(id);
        Usuario resposta =
                usuarioService.atualizar(usuario);
        return ResponseEntity.ok(resposta);
    }
}
