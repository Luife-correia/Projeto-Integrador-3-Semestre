package com.gamebox.app.Domain;

import com.gamebox.app.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity // Indica que essa classe representa uma entidade do banco de dados.
@Table(name = "Usuarios")
public class Usuario {

    // O banco gera o ID automaticamente ao cadastrar um usuário.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O nome é obrigatório no banco e deve ter pelo menos 5 caracteres.
    @Column(nullable = false)
    @Size(min = 5, message = "O nome deve ter pelo menos 5 caracteres")
    private String nome;


    // Valida se o e-mail foi preenchido e se possui um formato válido.
    // Também impede que dois usuários tenham o mesmo e-mail no banco.
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    // Armazena o hash da senha, em vez da senha original.
    @NotBlank
    private String senhaHash;


    // Campo opcional para guardar o endereço da foto de perfil.
    private String fotoPerfil;

    // A bio deve ter entre 6 e 100 caracteres.
    @Size(min = 6, message = "A bio deve ter pelo menos 6 caracteres")
    @Size(max = 100, message = "A bio deve ter no maximo 100 caracteres")
    private String bio;


    // Salva o tipo de usuário, como administrador ou usuário comum.
    // O EnumType.STRING grava o nome da opção no banco, em vez do número.
    @Enumerated(EnumType.STRING)
    private Role role;

    // Guarda a data em que o usuário foi criado.
    private LocalDate dataCriacao;

    // Executa esse método automaticamente antes de inserir o usuário no banco.
    @PrePersist
    public void prePersist() {
        LocalDate agora = LocalDate.now();
        this.dataCriacao = agora;
    }

    // Um usuário pode ter várias listas de jogos.
    // O relacionamento é definido pelo atributo "usuario" na classe ListaJogos.
    @OneToMany(mappedBy = "usuario")
    private List<ListaJogos> listaJogos = new ArrayList<>();


    // Guarda os registros dos jogos associados ao usuário e seus status.
    // O cascade permite propagar operações para esses registros.
    // O orphanRemoval remove registros que forem desvinculados da coleção.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioJogo> statusDosJogos = new ArrayList<>();

    // Um usuário pode ter várias avaliações.
    @OneToMany(mappedBy = "usuario")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    // Construtor vazio utilizado pelo JPA para criar instâncias da entidade.
    public Usuario() {
    }

    // Construtor para inicializar um usuário com os dados principais.
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

    // Retorna o ID do usuário.
    public Long getId() {
        return id;
    }

    // Define o ID do usuário.
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
