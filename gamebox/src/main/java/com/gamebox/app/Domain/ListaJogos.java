package com.gamebox.app.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

// Representa uma lista de jogos cadastrada no sistema.
@Entity
@Table(name = "ListasJogos")
public class ListaJogos {

    // Identificador gerado automaticamente pelo banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada lista pertence a um usuário.
    // A coluna usuario_id guarda a referência ao dono da lista.
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Uma lista pode conter vários jogos, e um mesmo jogo pode
    // estar presente em diferentes listas.
    // A tabela intermediária registra essa associação.
    @ManyToMany
    @JoinTable(
            name = "listas_jogos_jogos",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> jogos = new ArrayList<>();

    @Column(nullable = false)
    @NotBlank
    private String nome;


    @Column(nullable = false)
    private Boolean publica;

    @Column(nullable = false)
    private boolean principal;

    public ListaJogos(Usuario usuario, @NotBlank(message = "O nome da lista é obrigatório") String nome, @NotNull(message = "A visibilidade da lista (pública/privada) é obrigatória") Boolean publica, boolean principal, List<Long> longs) {
    }

    public ListaJogos(
            Usuario usuario,
            List<Jogo> jogos,
            String nome,
            Boolean publica,
            boolean principal
    ) {
        this.usuario = usuario;
        this.jogos = jogos != null ? jogos : new ArrayList<>();
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
    public void setJogos(List<Long> jogos) {
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