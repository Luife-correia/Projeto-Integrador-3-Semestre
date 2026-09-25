package com.gamebox.app.Controller;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuarios")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<Usuario> salvar(
            @RequestBody @Valid Usuario usuario
    ) {

        Usuario resposta =
                userService.salvar(usuario);

        return ResponseEntity.ok(resposta);
    }

    // FAZER DELETE
}
