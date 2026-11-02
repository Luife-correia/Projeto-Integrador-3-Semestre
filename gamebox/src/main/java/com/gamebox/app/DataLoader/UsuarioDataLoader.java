package com.gamebox.app.DataLoader;

import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Enum.Role;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * DataLoader para a entidade Usuario.
 *
 * EXPLICAÇÃO DE ESTUDO:
 * - O que é um CommandLineRunner?
 *   É uma interface funcional do Spring Boot executada automaticamente logo após o container
 *   Spring estar completamente inicializado e o contexto da aplicação estar pronto.
 * - Para que serve?
 *   Ideal para popular o banco de dados (seed data) com registros iniciais essenciais para testes
 *   e desenvolvimento, como usuários administradores e usuários comuns.
 * - O uso de @Order(1):
 *   Define a ordem de execução dos DataLoaders. Como outras entidades (Listas, Avaliações,
 *   Relações Usuário-Jogo) dependem de usuários existentes no banco, o UsuarioDataLoader executa primeiro.
 * - Segurança (PasswordEncoder):
 *   As senhas nunca são salvas em texto plano. Utilizamos o PasswordEncoder para gerar o hash seguro (BCrypt).
 */
@Component
@Order(1)
public class UsuarioDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificação de idempotência: só insere se a tabela estiver vazia
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Populando banco de dados: Usuários iniciais...");

            // 1. Usuário Administrador
            Usuario admin = new Usuario(
                    null,
                    "Administrador GameBox",
                    "admin@gamebox.com",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN,
                    LocalDate.now().minusMonths(6)
            );
            admin.setFotoPerfil("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80");
            admin.setBio("Administrador principal do sistema GameBox.");
            usuarioRepository.save(admin);

            // 2. Usuário Comum 1
            Usuario usuario1 = new Usuario(
                    null,
                    "João Silva",
                    "joao@email.com",
                    passwordEncoder.encode("senha123"),
                    Role.USER,
                    LocalDate.now().minusMonths(3)
            );
            usuario1.setFotoPerfil("https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?auto=format&fit=crop&w=200&q=80");
            usuario1.setBio("Entusiasta de RPGs e jogos de mundo aberto.");
            usuarioRepository.save(usuario1);

            // 3. Usuário Comum 2
            Usuario usuario2 = new Usuario(
                    null,
                    "Maria Souza",
                    "maria@email.com",
                    passwordEncoder.encode("senha123"),
                    Role.USER,
                    LocalDate.now().minusMonths(1)
            );
            usuario2.setFotoPerfil("https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=200&q=80");
            usuario2.setBio("Fã de indies, metroidvanias e roguelikes.");
            usuarioRepository.save(usuario2);

            System.out.println(">>> Usuários cadastrados com sucesso! Total: " + usuarioRepository.count());
        } else {
            System.out.println(">>> Tabela de Usuários já possui registros. Ignorando seed.");
        }
    }
}
