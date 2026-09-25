package com.gamebox.app.Domain;


import jakarta.persistence.*;


import java.util.Date;
import java.util.List;

@Table(name = "Jogo")
@Entity
public class Jogo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String capa;

    @ElementCollection
    private List<String> plataformas;

    @ElementCollection
    private List<String> generos;

    @Column(nullable = false)
    private String desenvolvedora;

    @Column(nullable = false)
    private Date dataLancamento;

    @Column(nullable = false)
    private String sinopse;

    public Jogo() {
    }

    public Jogo(String nome, String capa, List<String> plataformas, List<String> generos, String desenvolvedora, Date dataLancamento, String sinopse) {
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

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
}
