package com.gamebox.app.Domain;


import jakarta.persistence.*;

@Entity
@Table(name = "biblioteca")
public class BibliotecaJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany
    @JoinColumn(name = "jogos_id")
    private Jogo jogo;

    private Float nota;

    private int HorasJogadas;


    public BibliotecaJogo(Long id, Usuario usuario, Jogo jogo, Float nota, int horasJogadas) {
        this.id = id;
        this.usuario = usuario;
        this.jogo = jogo;
    }

    public BibliotecaJogo (){}

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

    public Float getNota() {
        return nota;
    }
    public void setNota(Float nota) {
        this.nota = nota;
    }

    public int getHorasJogadas() {
        return HorasJogadas;
    }
    public void setHorasJogadas(int horasJogadas) {
        HorasJogadas = horasJogadas;
    }
}
