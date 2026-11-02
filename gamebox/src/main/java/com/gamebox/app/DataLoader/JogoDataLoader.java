package com.gamebox.app.DataLoader;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Repository.JogoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * DataLoader para a entidade Jogo.
 *
 * EXPLICAÇÃO DE ESTUDO:
 * - O que faz?
 *   Cadastra um catálogo inicial de jogos reais com URLs de capas da web, plataformas,
 *   gêneros, desenvolvedoras, datas de lançamento e sinopses.
 * - Ordem (@Order(2)):
 *   Executa após os usuários, pois as listas de jogos (`ListaJogos`) e interações de usuário (`UsuarioJogo`)
 *   precisam referenciar estes jogos cadastrados.
 * - URLs de Jogos na Web:
 *   Contém links reais e estáveis de capas e metadados de jogos populares da indústria.
 */
@Component
@Order(2)
public class JogoDataLoader implements CommandLineRunner {

    private final JogoRepository jogoRepository;

    public JogoDataLoader(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (jogoRepository.count() == 0) {
            System.out.println(">>> Populando banco de dados: Catálogo de Jogos...");

            // 1. Elden Ring
            Jogo jogo1 = new Jogo(
                    "Elden Ring",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/1245620/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 5", "Xbox Series X"),
                    Arrays.asList("RPG", "Action", "Open World"),
                    "FromSoftware",
                    LocalDate.of(2022, 2, 25),
                    "Um RPG de ação em mundo aberto ambientado em um vasto universo fantástico criado por Hidetaka Miyazaki e George R. R. Martin."
            );
            jogoRepository.save(jogo1);

            // 2. The Witcher 3: Wild Hunt
            Jogo jogo2 = new Jogo(
                    "The Witcher 3: Wild Hunt",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/292030/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 4", "Xbox One", "Nintendo Switch"),
                    Arrays.asList("RPG", "Adventure", "Fantasy"),
                    "CD Projekt Red",
                    LocalDate.of(2015, 5, 19),
                    "Geralt de Rívia busca sua filha adotiva em um mundo de fantasia medieval rico em escolhas morais e monstros perigosos."
            );
            jogoRepository.save(jogo2);

            // 3. Batman: Arkham Knight
            Jogo jogo3 = new Jogo(
                    "Batman: Arkham Knight",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/208650/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 4", "Xbox One"),
                    Arrays.asList("Action", "Adventure", "Open World"),
                    "Rocksteady Studios",
                    LocalDate.of(2015, 6, 23),
                    "Batman enfrenta o misterioso Cavaleiro de Arkham e seus aliados para proteger Gotham em uma aventura de ação em mundo aberto."
            );
            jogoRepository.save(jogo3);

            // 4. Baldur's Gate 3
            Jogo jogo4 = new Jogo(
                    "Baldur's Gate 3",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 5", "Xbox Series X"),
                    Arrays.asList("RPG", "Strategy", "Turn-Based"),
                    "Larian Studios",
                    LocalDate.of(2023, 8, 3),
                    "Um RPG baseado nas regras de Dungeons & Dragons, oferecendo escolhas profundas, narrativa ramificada e combate tático."
            );
            jogoRepository.save(jogo4);

            // 5. Red Dead Redemption 2
            Jogo jogo5 = new Jogo(
                    "Red Dead Redemption 2",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/1174180/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 4", "Xbox One"),
                    Arrays.asList("Action", "Adventure", "Open World"),
                    "Rockstar Games",
                    LocalDate.of(2018, 10, 26),
                    "A epopeia trágica de Arthur Morgan e a gangue de Dutch van der Linde no declínio do Velho Oeste americano."
            );
            jogoRepository.save(jogo5);

            // 6. God of War Ragnarök
            Jogo jogo6 = new Jogo(
                    "God of War Ragnarök",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/2322010/library_600x900.jpg",
                    Arrays.asList("PlayStation 4", "PlayStation 5", "PC"),
                    Arrays.asList("Action", "Adventure", "Hack and Slash"),
                    "Santa Monica Studio",
                    LocalDate.of(2022, 11, 9),
                    "Kratos e Atreus embarcam em uma jornada mítica pelos Nove Reinos em busca de respostas antes que o Ragnarök destrua o mundo."
            );
            jogoRepository.save(jogo6);

            // 7. Hollow Knight
            Jogo jogo7 = new Jogo(
                    "Hollow Knight",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/367520/library_600x900.jpg",
                    Arrays.asList("PC", "Nintendo Switch", "PlayStation 4", "Xbox One"),
                    Arrays.asList("Metroidvania", "Action", "Indie"),
                    "Team Cherry",
                    LocalDate.of(2017, 2, 24),
                    "Explore um vasto reino subterrâneo em ruínas de insetos e heróis, enfrentando criaturas corajosas e mistérios ancestrais."
            );
            jogoRepository.save(jogo7);

            // 8. Hades
            Jogo jogo8 = new Jogo(
                    "Hades",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/1145360/library_600x900.jpg",
                    Arrays.asList("PC", "Nintendo Switch", "PlayStation", "Xbox"),
                    Arrays.asList("Roguelike", "Action", "Indie"),
                    "Supergiant Games",
                    LocalDate.of(2020, 9, 17),
                    "Lute para escapar do Submundo do Inferno grego na pele de Zagreus, o príncipe rebelde dos mortos."
            );
            jogoRepository.save(jogo8);

            // 9. Cyberpunk 2077
            Jogo jogo9 = new Jogo(
                    "Cyberpunk 2077",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/1091500/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 5", "Xbox Series X"),
                    Arrays.asList("RPG", "Action", "Sci-Fi"),
                    "CD Projekt Red",
                    LocalDate.of(2020, 12, 10),
                    "Um RPG de ação e aventura em mundo aberto ambientado na megalópole futurista de Night City."
            );
            jogoRepository.save(jogo9);

            // 10. Grand Theft Auto V
            Jogo jogo10 = new Jogo(
                    "Grand Theft Auto V",
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/271590/library_600x900.jpg",
                    Arrays.asList("PC", "PlayStation 4", "Xbox One", "PlayStation 5"),
                    Arrays.asList("Action", "Open World", "Driving"),
                    "Rockstar North",
                    LocalDate.of(2013, 9, 17),
                    "Três criminosos diferentes planejam e executam assaltos arriscados em Los Santos enquanto lidam com agências governamentais."
            );
            jogoRepository.save(jogo10);

            System.out.println(">>> Catálogo de Jogos cadastrado com sucesso! Total: " + jogoRepository.count());
        } else {
            System.out.println(">>> Tabela de Jogos já possui registros. Ignorando seed.");
        }
    }
}
