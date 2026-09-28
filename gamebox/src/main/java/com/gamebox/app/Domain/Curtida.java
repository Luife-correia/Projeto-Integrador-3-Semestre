package com.gamebox.app.Domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "Curtidas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_id", "jogo_id"})
    }
)
@Entity
public class Curtida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(nullable = false)
    private LocalDate dataCriacao;


    public Curtida() {
    }

    public Curtida(Long id, Usuario usuario, Jogo jogo, LocalDate dataCriacao) {
        this.id = id;
        this.usuario = usuario;
        this.jogo = jogo;
        this.dataCriacao = dataCriacao;
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}


