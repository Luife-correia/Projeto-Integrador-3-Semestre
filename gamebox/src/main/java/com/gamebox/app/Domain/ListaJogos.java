package com.gamebox.app.Domain;

import com.gamebox.app.Enum.StatusListaJogos;
import jakarta.persistence.*;

@Table(name = "ListasJogos")
@Entity
public class ListaJogos {

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
    private String nome;

    @Column(nullable = false)
    private Boolean publica;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusListaJogos status;

    public ListaJogos() {
    }

    public ListaJogos(Long id, Usuario usuario, Jogo jogo, String nome, Boolean publica,
                      StatusListaJogos status) {
        this.id = id;
        this.usuario = usuario;
        this.jogo = jogo;
        this.nome = nome;
        this.publica = publica;
        this.status = status;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public StatusListaJogos getStatus() {
        return status;
    }

    public void setStatus(StatusListaJogos status) {
        this.status = status;
    }
}
