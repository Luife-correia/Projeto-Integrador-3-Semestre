package com.gamebox.app.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @Column(nullable = false)
    @Min(value = 0, message = "A nota deve ser no mínimo 0")
    @Max(value = 10, message = "A nota deve ser no máximo 10")
    private int notaEmMeiasEstrelas;

    private String comentario;

    private LocalDate dataCriacao;


    public Avaliacao() {
    }

    public Avaliacao(Usuario usuario, Jogo jogo, int notaEmMeiasEstrelas, String comentario, LocalDate dataCriacao) {
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
