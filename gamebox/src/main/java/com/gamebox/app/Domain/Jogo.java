
package com.gamebox.app.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String capa;

    @ElementCollection
    private List<String> plataformas = new ArrayList<>();

    @ElementCollection
    private List<String> generos = new ArrayList<>();

    @Column(nullable = false)
    private String desenvolvedora;

    @Column(nullable = false)
    private LocalDate dataLancamento;

    @Column(nullable = false)
    private String sinopse;

    @ManyToMany(mappedBy = "jogos")
    private List<ListaJogos> listas = new ArrayList<>();

    // Mantém os status deste jogo para cada usuário que o possui na biblioteca.
    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioJogo> statusPorUsuario = new ArrayList<>();

    @Column(nullable = false)
    private double notaTotal;

    public Jogo() {
    }

    public Jogo(
            String nome,
            String capa,
            List<String> plataformas,
            List<String> generos,
            String desenvolvedora,
            LocalDate dataLancamento,
            String sinopse
    ) {
        this.nome = nome;
        this.capa = capa;
        this.plataformas = plataformas;
        this.generos = generos;
        this.desenvolvedora = desenvolvedora;
        this.dataLancamento = dataLancamento;
        this.sinopse = sinopse;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public List<String> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public String getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(String desenvolvedora) {
        this.desenvolvedora = desenvolvedora;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public List<ListaJogos> getListas() {
        return listas;
    }

    public void setListas(List<ListaJogos> listas) {
        this.listas = listas;
    }

    public List<UsuarioJogo> getStatusPorUsuario() {
        return statusPorUsuario;
    }

    public void setStatusPorUsuario(List<UsuarioJogo> statusPorUsuario) {
        this.statusPorUsuario = statusPorUsuario;
    }

    public double getNotaTotal() {
        return notaTotal;
    }

    public void atualizarNota(
            int somaTotalAvaliacoes,
            long totalAvaliacoes
    ) {
        if (totalAvaliacoes <= 0) {
            this.notaTotal = 0.0;
            return;
        }

        this.notaTotal =
                ((double) somaTotalAvaliacoes / totalAvaliacoes) / 2;
    }
}
