package com.gamebox.app.Domain;

import com.gamebox.app.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Table(name = "Usuarios")
@Entity
public class Usuario {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, message = "O nome deve ter pelo menos 5 caracteres")
    private String nome;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senhaHash;

    private String fotoPerfil;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Date dataCriacao;

    private boolean ativo;

    /**
     ********** Constructor **********
     */

    public Usuario() {
    }

    public Usuario(
            Long id,
            String nome,
            String email,
            String senhaHash,
            Role role,
            Date dataCriacao,
            boolean ativo
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.role = role;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    /**
     ********** GET E SET  **********
     */

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

    public Date getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
