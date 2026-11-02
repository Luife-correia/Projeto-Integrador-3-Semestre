package com.gamebox.app.DataLoader;

import com.gamebox.app.Domain.Avaliacao;
import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Repository.AvaliacaoRepository;
import com.gamebox.app.Repository.JogoRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * DataLoader para a entidade Avaliacao.
 *
 * EXPLICAÇÃO DE ESTUDO:
 * - O que faz?
 *   Cria avaliações e comentários feitos por usuários sobre jogos do catálogo.
 * - Ordem (@Order(5)):
 *   Executa por último, pois depende diretamente de instâncias válidas de `Usuario` e `Jogo`.
 * - Atualização de nota do Jogo:
 *   Demonstra como avaliações podem influenciar a nota total dos jogos.
 */
@Component
@Order(5)
public class AvaliacaoDataLoader implements CommandLineRunner {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;

    public AvaliacaoDataLoader(AvaliacaoRepository avaliacaoRepository,
                               UsuarioRepository usuarioRepository,
                               JogoRepository jogoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoRepository = jogoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (avaliacaoRepository.count() == 0) {
            System.out.println(">>> Populando banco de dados: Avaliações de Jogos...");

            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Jogo> jogos = jogoRepository.findAll();

            if (!usuarios.isEmpty() && !jogos.isEmpty()) {
                Usuario joao = usuarios.stream().filter(u -> u.getEmail().equals("joao@email.com")).findFirst().orElse(usuarios.get(0));
                Usuario maria = usuarios.stream().filter(u -> u.getEmail().equals("maria@email.com")).findFirst().orElse(usuarios.get(0));

                // Avaliação do João para Elden Ring
                if (jogos.size() > 0) {
                    Avaliacao av1 = new Avaliacao(
                            joao,
                            jogos.get(0),
                            10, // 5 estrelas
                            "Simplesmente uma obra-prima dos jogos de mundo aberto. Desafiador e recompensador!",
                            LocalDate.now().minusDays(10)
                    );
                    avaliacaoRepository.save(av1);
                    jogos.get(0).atualizarNota(10, 1);
                    jogoRepository.save(jogos.get(0));
                }

                // Avaliação da Maria para Hollow Knight
                if (jogos.size() > 6) {
                    Avaliacao av2 = new Avaliacao(
                            maria,
                            jogos.get(6),
                            9, // 4,5 estrelas
                            "Arte maravilhosa, trilha sonora impecável e desafio na medida certa. Indispensável!",
                            LocalDate.now().minusDays(5)
                    );
                    avaliacaoRepository.save(av2);
                    jogos.get(6).atualizarNota(9, 1);
                    jogoRepository.save(jogos.get(6));
                }

                System.out.println(">>> Avaliações cadastradas com sucesso! Total: " + avaliacaoRepository.count());
            }
        } else {
            System.out.println(">>> Tabela de Avaliações já possui registros. Ignorando seed.");
        }
    }
}
