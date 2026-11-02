package com.gamebox.app.DataLoader;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Domain.UsuarioJogo;
import com.gamebox.app.Enum.StatusJogos;
import com.gamebox.app.Repository.JogoRepository;
import com.gamebox.app.Repository.UsuarioJogoRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataLoader para a entidade UsuarioJogo.
 *
 * EXPLICAÇÃO DE ESTUDO:
 * - O que faz?
 *   Associa usuários a jogos específicos definindo o status de gameplay de cada um (ex: JOGANDO, JOGADO, QUERO_JOGAR).
 * - Ordem (@Order(4)):
 *   Executa após usuários e jogos existirem.
 */
@Component
@Order(4)
public class UsuarioJogoDataLoader implements CommandLineRunner {

    private final UsuarioJogoRepository usuarioJogoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;

    public UsuarioJogoDataLoader(UsuarioJogoRepository usuarioJogoRepository,
                                  UsuarioRepository usuarioRepository,
                                  JogoRepository jogoRepository) {
        this.usuarioJogoRepository = usuarioJogoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoRepository = jogoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioJogoRepository.count() == 0) {
            System.out.println(">>> Populando banco de dados: Vínculos Usuário-Jogo (Status)...");

            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Jogo> jogos = jogoRepository.findAll();

            if (!usuarios.isEmpty() && !jogos.isEmpty()) {
                Usuario joao = usuarios.stream().filter(u -> u.getEmail().equals("joao@email.com")).findFirst().orElse(usuarios.get(0));
                Usuario maria = usuarios.stream().filter(u -> u.getEmail().equals("maria@email.com")).findFirst().orElse(usuarios.get(0));

                // João jogando Elden Ring
                if (jogos.size() > 0) {
                    UsuarioJogo uj1 = new UsuarioJogo(joao, jogos.get(0));
                    uj1.setStatus(StatusJogos.JOGANDO);
                    usuarioJogoRepository.save(uj1);
                }

                // João já zerou The Witcher 3
                if (jogos.size() > 1) {
                    UsuarioJogo uj2 = new UsuarioJogo(joao, jogos.get(1));
                    uj2.setStatus(StatusJogos.JOGADO);
                    usuarioJogoRepository.save(uj2);
                }

                // Maria quer jogar Hollow Knight
                if (jogos.size() > 6) {
                    UsuarioJogo uj3 = new UsuarioJogo(maria, jogos.get(6));
                    uj3.setStatus(StatusJogos.QUERO_JOGAR);
                    usuarioJogoRepository.save(uj3);
                }

                System.out.println(">>> Vínculos Usuário-Jogo cadastrados com sucesso! Total: " + usuarioJogoRepository.count());
            }
        } else {
            System.out.println(">>> Tabela UsuarioJogo já possui registros. Ignorando seed.");
        }
    }
}
