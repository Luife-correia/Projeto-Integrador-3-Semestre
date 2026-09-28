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
}
