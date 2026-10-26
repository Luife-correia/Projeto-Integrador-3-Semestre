package com.gamebox.app.Domain;

import com.gamebox.app.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Usuarios")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, message = "O nome deve ter pelo menos 5 caracteres")
    private String nome;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    @Size(max = 10, message = "A senha deve ter no maximo 10 caracteres")
    private String senhaHash;

    private String fotoPerfil;

    @Size(min = 6, message = "A bio deve ter pelo menos 6 caracteres")
    @Size(max = 100, message = "A bio deve ter no maximo 100 caracteres")
    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate dataCriacao;

    @PrePersist
    public void prePersist() {
        LocalDate agora = LocalDate.now();
        this.dataCriacao = agora;
    }

    @OneToMany(mappedBy = "usuario")
    private List<ListaJogos> listaJogos = new ArrayList<>();

    // Um registro por jogo do usuário, contendo o status individual.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioJogo> statusDosJogos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    // Construtor
    public Usuario() {
    }

    public Usuario(
            Long id,
            String nome,
            String email,
            String senhaHash,
            Role role,
            LocalDate dataCriacao
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.role = role;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<ListaJogos> getListaJogos() {
        return listaJogos;
    }

    public void setListaJogos(List<ListaJogos> listaJogos) {
        this.listaJogos = listaJogos;
    }

    public List<UsuarioJogo> getStatusDosJogos() {
        return statusDosJogos;
    }

    public void setStatusDosJogos(List<UsuarioJogo> statusDosJogos) {
        this.statusDosJogos = statusDosJogos;
    }

}
