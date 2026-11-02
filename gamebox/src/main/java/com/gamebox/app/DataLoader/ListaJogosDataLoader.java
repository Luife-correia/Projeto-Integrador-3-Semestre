package com.gamebox.app.DataLoader;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Repository.JogoRepository;
import com.gamebox.app.Repository.ListaJogosRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataLoader para a entidade ListaJogos.
 *
 * EXPLICAÇÃO DE ESTUDO:
 * - O que faz?
 *   Cria listas personalizadas de jogos associadas aos usuários (ex: Biblioteca Principal, Favoritos, Quero Jogar).
 * - Ordem (@Order(3)):
 *   Executa após `Usuario` e `Jogo` estarem salvos, pois `ListaJogos` possui relacionamento `@ManyToOne` com `Usuario`
 *   e relacionamento `@ManyToMany` com `Jogo`.
 */
@Component
@Order(3)
public class ListaJogosDataLoader implements CommandLineRunner {

    private final ListaJogosRepository listaJogosRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;

    public ListaJogosDataLoader(ListaJogosRepository listaJogosRepository,
                                UsuarioRepository usuarioRepository,
                                JogoRepository jogoRepository) {
        this.listaJogosRepository = listaJogosRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoRepository = jogoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (listaJogosRepository.count() == 0) {
            System.out.println(">>> Populando banco de dados: Listas de Jogos...");

            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Jogo> jogos = jogoRepository.findAll();

            if (!usuarios.isEmpty() && !jogos.isEmpty()) {
                Usuario joao = usuarios.stream().filter(u -> u.getEmail().equals("joao@email.com")).findFirst().orElse(usuarios.get(0));
                Usuario maria = usuarios.stream().filter(u -> u.getEmail().equals("maria@email.com")).findFirst().orElse(usuarios.get(0));

                // 1. Biblioteca Principal do João
                ListaJogos bibliotecaJoao = new ListaJogos(
                        joao,
                        jogos.subList(0, Math.min(5, jogos.size())),
                        "Biblioteca Principal",
                        true,
                        true
                );
                listaJogosRepository.save(bibliotecaJoao);

                // 2. Lista de Favoritos do João
                ListaJogos favoritosJoao = new ListaJogos(
                        joao,
                        jogos.subList(0, Math.min(2, jogos.size())),
                        "Meus Favoritos",
                        true,
                        false
                );
                listaJogosRepository.save(favoritosJoao);

                // 3. Biblioteca Principal da Maria
                ListaJogos bibliotecaMaria = new ListaJogos(
                        maria,
                        jogos.subList(5, Math.min(9, jogos.size())),
                        "Biblioteca Principal",
                        true,
                        true
                );
                listaJogosRepository.save(bibliotecaMaria);

                System.out.println(">>> Listas de Jogos cadastradas com sucesso! Total: " + listaJogosRepository.count());
            }
        } else {
            System.out.println(">>> Tabela de Listas de Jogos já possui registros. Ignorando seed.");
        }
    }
}
