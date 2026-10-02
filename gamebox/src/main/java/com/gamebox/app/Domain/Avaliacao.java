package com.gamebox.app.Domain;

import jakarta.persistence.*;

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






//Exatamente. 👍
//
//Você pode pensar na nota como 0 a 10 unidades de meia estrela:
//
//Valor armazenado	Visual
//0	☆☆☆☆☆
//        1	★☆☆☆☆
//        2	★☆☆☆☆
//        3	★★☆☆☆
//        4	★★☆☆☆
//        5	★★★☆☆
//        6	★★★☆☆
//        7	★★★★☆
//        8	★★★★☆
//        9	★★★★★
//        10	★★★★★
//
//Mas cuidado: a tabela acima mostra a quantidade de meias-estrelas, então visualmente precisamos distinguir 1 de 2:
//
//        0 → ☆☆☆☆☆
//        1 → ☆☆☆☆☆ + meia estrela
//2 → ★☆☆☆☆
//        3 → ★☆☆☆☆ + meia estrela
//4 → ★★☆☆☆
//        ...
//        10 → ★★★★★
//
//Então sua modelagem pode ser:
//
//private int nota;
//
//com a regra:
//
//        0 <= nota <= 10
//
//E no frontend:
//
//nota / 2 = quantidade de estrelas
//
//Por exemplo:
//
//nota = 7
//        7 / 2 = 3.5 estrelas
//
//E isso deixa seu botão muito simples
//+
//
//nota = nota + 1
//
//        -
//
//nota = nota - 1
//
//Porque cada 1 representa meia estrela.
//
//E os limites:
//
//nota == 10 → não aumenta
//nota == 0  → não diminui
//
//Isso é uma modelagem bem legal porque você mantém int no Java e no banco, mas consegue representar perfeitamente as meias estrelas.
//
//Agora podemos voltar para sua Avaliacao e decidir onde essas regras (0–10) devem ser aplicadas: entidade, Service ou ambos. Esse é um ótimo próximo passo para você aprender Spring Boot.