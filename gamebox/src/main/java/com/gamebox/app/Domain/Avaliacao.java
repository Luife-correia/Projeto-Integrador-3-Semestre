package com.gamebox.app.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "avaliacoes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_avaliacao_usuario_jogo",
                columnNames = {"usuario_id", "jogo_id"}
        )
)
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(nullable = false)
    @Min(value = 0, message = "A nota deve ser no mínimo 0")
    @Max(value = 10, message = "A nota deve ser no máximo 10")
    private int notaEmMeiasEstrelas;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    @Size(min = 10, message = "O comentário deve ter no mínimo 10 caracteres")
    private String comentario;

    private LocalDate dataCriacao;

    @PrePersist
    public void prePersist() {
        LocalDate agora = LocalDate.now();
        this.dataCriacao = agora;
    }

    public Avaliacao() {
    }

    public Avaliacao(
            Usuario usuario,
            Jogo jogo,
            int notaEmMeiasEstrelas,
            String comentario,
            LocalDate dataCriacao
    ) {
        this.usuario = usuario;
        this.jogo = jogo;
        this.notaEmMeiasEstrelas = notaEmMeiasEstrelas;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNotaEmMeiasEstrelas() {
        return notaEmMeiasEstrelas;
    }

    public void setNotaEmMeiasEstrelas(int notaEmMeiasEstrelas) {
        this.notaEmMeiasEstrelas = notaEmMeiasEstrelas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
