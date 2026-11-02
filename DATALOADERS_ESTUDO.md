# Guia de Estudo: DataLoaders no Projeto GameBox

Este documento explica detalhadamente o funcionamento dos **DataLoaders** implementados no projeto **GameBox**, criados para popular o banco de dados (seed data) com dados iniciais de estudo e testes.

---

## O que é um DataLoader no Spring Boot?

Um **DataLoader** (ou inicializador de dados) é um componente executado automaticamente pelo Spring Boot logo após a inicialização do contexto da aplicação. 

No Spring Boot, implementamos a interface funcional **`CommandLineRunner`** (ou `ApplicationRunner`), que fornece o método `run(String... args)`. Tudo o que é colocado dentro desse método é executado uma única vez quando a aplicação sobe.

### Vantagens:
1. **Ambiente pronto para uso**: Ao iniciar a aplicação (mesmo com banco em memória H2), o sistema já vem populado com usuários, jogos, listas e avaliações.
2. **Idempotência**: Cada DataLoader verifica se a tabela correspondente já possui registros (`repository.count() == 0`) antes de inserir novos dados. Isso evita erros de chave duplicada ou duplicação de dados ao reiniciar a aplicação.
3. **Ordem de Execução (`@Order`)**: Como as entidades possuem relacionamentos de chave estrangeira (`@ManyToOne`, `@ManyToMany`), usamos a anotação `@Order(n)` para garantir que as entidades independentes sejam criadas antes das dependentes.

---

## Detalhamento de Cada DataLoader

### 1. `UsuarioDataLoader` (`@Order(1)`)
* **Propósito**: Cadastra os usuários iniciais do sistema (Administrador e usuários comuns).
* **Funcionamento**:
  - Injeta o `UsuarioRepository` e o `PasswordEncoder`.
  - Verifica se a tabela `Usuarios` está vazia.
  - Cria usuários definindo nome, e-mail, senha criptografada via BCrypt (`passwordEncoder.encode(...)`), perfil (`Role.ADMIN` ou `Role.USER`), foto de perfil e biografia.
* **Por que é o primeiro?**: Listas de jogos, status de gameplay e avaliações dependem de um usuário existente no banco (`usuario_id`).

### 2. `JogoDataLoader` (`@Order(2)`)
* **Propósito**: Cadastra o catálogo inicial de jogos com capas reais da web (URLs de CDNs públicas), plataformas, gêneros, desenvolvedoras, datas de lançamento e sinopses.
* **Funcionamento**:
  - Injeta o `JogoRepository`.
  - Cadastra jogos icônicos da indústria (ex: *Elden Ring*, *The Witcher 3*, *Minecraft*, *Baldur's Gate 3*, *Red Dead Redemption 2*, *God of War Ragnarök*, *Hollow Knight*, *Hades*, *Cyberpunk 2077*, *Grand Theft Auto V*).
  - Utiliza listas (`List<String>`) para plataformas e gêneros (`@ElementCollection` no JPA).
* **Por que executa cedo?**: Listas de jogos e vínculos de usuário (`UsuarioJogo`) precisam referenciar os IDs dos jogos cadastrados.

### 3. `ListaJogosDataLoader` (`@Order(3)`)
* **Propósito**: Cria listas personalizadas de jogos associadas aos usuários (ex: *Biblioteca Principal*, *Meus Favoritos*).
* **Funcionamento**:
  - Injeta `ListaJogosRepository`, `UsuarioRepository` e `JogoRepository`.
  - Busca os usuários e jogos já cadastrados.
  - Associa um subconjunto de jogos a cada lista através do relacionamento `@ManyToMany` (`listas_jogos_jogos`) e define o dono da lista (`@ManyToOne`).

### 4. `UsuarioJogoDataLoader` (`@Order(4)`)
* **Propósito**: Registra a relação de posse/status de gameplay entre usuários e jogos (`UsuarioJogo`).
* **Funcionamento**:
  - Injeta `UsuarioJogoRepository`, `UsuarioRepository` e `JogoRepository`.
  - Define o status de gameplay (`StatusJogos.JOGANDO`, `StatusJogos.JOGADO`, `StatusJogos.QUERO_JOGAR`).
  - Garante a restrição de unicidade por par `(usuario_id, jogo_id)`.

### 5. `AvaliacaoDataLoader` (`@Order(5)`)
* **Propósito**: Adiciona avaliações, notas e comentários feitos por usuários sobre os jogos.
* **Funcionamento**:
  - Injeta `AvaliacaoRepository`, `UsuarioRepository` e `JogoRepository`.
  - Cria registros na tabela `avaliacoes` vinculando um usuário a um jogo com nota e comentário.
* **Por que é o último?**: Depende de instâncias válidas de `Usuario` e `Jogo` já persistidas no banco.

---

## Como Testar

1. Inicie a aplicação Spring Boot (via IDE ou Maven).
2. Observe os logs no terminal indicando a execução de cada DataLoader:
   - `>>> Populando banco de dados: Usuários iniciais...`
   - `>>> Populando banco de dados: Catálogo de Jogos...`
   - etc.
3. Acesse o console H2 em `http://localhost:8080/h2-console` (conforme `application.properties`) para visualizar as tabelas e dados populados.
