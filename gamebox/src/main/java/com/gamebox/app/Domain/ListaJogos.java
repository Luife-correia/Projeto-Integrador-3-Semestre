package com.gamebox.app.Domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ListasJogos")
public class ListaJogos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "listas_jogos_jogos",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> jogos = new ArrayList<>();

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean publica;

    // Diferencia a biblioteca fixa do usuário das listas personalizadas.
    @Column(nullable = false)
    private boolean principal;

    public ListaJogos() {
    }

    public ListaJogos(
            Usuario usuario,
            List<Jogo> jogos,
            String nome,
            Boolean publica,
            boolean principal
    ) {
        this.usuario = usuario;
        this.jogos = jogos;
        this.nome = nome;
        this.publica = publica;
        this.principal = principal;
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

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
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

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}