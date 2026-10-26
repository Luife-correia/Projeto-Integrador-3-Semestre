package com.gamebox.app.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

    // Nome que identifica a lista.
    @Column(nullable = false)
    @NotBlank
    private String nome;

    // Indica se a lista pode ser vista por outros usuários.
    @Column(nullable = false)
    private Boolean publica;

    // Diferencia a biblioteca principal das listas criadas pelo usuário.
    @Column(nullable = false)
    private boolean principal;

    // Construtor vazio utilizado pelo JPA.
    public ListaJogos() {
    }

    // Permite criar uma lista já informando seus dados principais.
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

    // Retorna o identificador da lista.
    public Long getId() {
        return id;
    }

    // Retorna o usuário dono da lista.
    public Usuario getUsuario() {
        return usuario;
    }

    // Define quem é o dono da lista.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Retorna os jogos que fazem parte da lista.
    public List<Jogo> getJogos() {
        return jogos;
    }

    // Atualiza os jogos da lista.
    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

    // Retorna o nome da lista.
    public String getNome() {
        return nome;
    }

    // Define o nome da lista.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Informa se a lista é pública.
    public Boolean getPublica() {
        return publica;
    }

    // Altera a visibilidade da lista.
    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    // Informa se esta é a biblioteca principal do usuário.
    public boolean isPrincipal() {
        return principal;
    }

    // Define se a lista é a principal.
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}