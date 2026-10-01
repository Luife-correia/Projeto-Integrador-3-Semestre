package com.gamebox.app.Domain;

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

    @OneToMany
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean publica;


    public ListaJogos() {
    }

    public ListaJogos(Long id, Usuario usuario, Jogo jogo, String nome, Boolean publica) {
        this.id = id;
        this.usuario = usuario;
        this.jogo = jogo;
        this.nome = nome;
        this.publica = publica;
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
}
