package com.gamebox.app.Domain;

import com.gamebox.app.Enum.StatusJogos;
import jakarta.persistence.*;

@Entity
@Table(
        name = "usuarios_jogos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_usuario_jogo",
                columnNames = {"usuario_id", "jogo_id"}
        )
)
public class UsuarioJogo {

    // Representa o vínculo entre um usuário e um jogo do catálogo.
    // O status pertence a este vínculo, pois varia de usuário para usuário.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusJogos status = StatusJogos.NAO_JOGADO;

    public UsuarioJogo() {
    }

    public UsuarioJogo(Usuario usuario, Jogo jogo) {
        this.usuario = usuario;
        this.jogo = jogo;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public StatusJogos getStatus() {
        return status;
    }

    public void setStatus(StatusJogos status) {
        this.status = status;
    }
}
