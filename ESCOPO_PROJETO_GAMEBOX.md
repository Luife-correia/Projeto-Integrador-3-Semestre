# Documento de Escopo Atualizado — Projeto GameBox

Este documento apresenta a especificação técnica e funcional atualizada do projeto **GameBox**, baseada rigorosamente na implementação atual do backend Java com Spring Boot.

---

## 1. Objetivo do Projeto

O **GameBox** é uma plataforma web voltada para entusiastas de jogos eletrônicos. A aplicação permite que:
- **Visitantes e Usuários** naveguem livremente pelo catálogo de jogos sem necessidade de autenticação.
- **Usuários cadastrados** realizem login, gerenciem sua biblioteca pessoal de jogos, criem listas personalizadas (públicas ou privadas), avaliem jogos com notas e comentários, e acompanhem o progresso de cada título.
- **Administradores** gerenciem usuários e jogos, acompanhando métricas gerais do sistema.

---

## 2. Visão Geral do Backend

O backend é desenvolvido em **Java 21** utilizando o framework **Spring Boot 4.1.1** (Spring Data JPA, Spring Security, Spring Web) e banco de dados **H2** (em memória para desenvolvimento e testes). A arquitetura é organizada em camadas bem definidas:

1. **Controller (`com.gamebox.app.Controller`):** Camada de apresentação/API REST. Expõe os endpoints HTTP (`@RestController`), recebe requisições, valida payloads (`@Valid`) e retorna respostas encapsuladas em `ResponseEntity`.
2. **Service (`com.gamebox.app.Service`):** Camada de regras de negócio. Contém a lógica de processamento, validações de negócio, regras de cálculo (ex: cálculo de notas e status) e orquestração entre repositórios.
3. **Repository (`com.gamebox.app.Repository`):** Camada de persistência. Interfaces que estendem `JpaRepository` para realizar operações de banco de dados sem necessidade de implementação manual de SQL.
4. **Domain (`com.gamebox.app.Domain`):** Entidades de domínio JPA mapeadas para tabelas relacionais no banco de dados (`Usuario`, `Jogo`, `UsuarioJogo`, `Avaliacao`, `ListaJogos`).
5. **DTO (`com.gamebox.app.Dto`):** Objetos de transferência de dados (Request e Response) para desacoplar a API das entidades de domínio e garantir segurança/clareza nos contratos.
6. **Security (`com.gamebox.app.Security`):** Configurações de segurança da aplicação (`SecurityConfig`), incluindo codificação de senhas (`BCryptPasswordEncoder`) e liberação de rotas públicas e do Swagger UI.
7. **Enum (`com.gamebox.app.Enum`):** Enumerações para papéis de usuário (`Role`) e status de progresso dos jogos (`StatusJogos`).

---

## 3. Entidades do Domínio

### 3.1. `Usuario` (Tabela: `Usuarios`)
Representa os usuários cadastrados na plataforma.
- **Atributos:**
  - `id` (`Long`): Identificador único gerado automaticamente (`IDENTITY`).
  - `nome` (`String`): Nome do usuário (mínimo de 5 caracteres, obrigatório).
  - `email` (`String`): E-mail único e obrigatório (validado por `@Email`).
  - `senhaHash` (`String`): Hash criptografado da senha (`BCrypt`).
  - `fotoPerfil` (`String`): URL ou caminho opcional da foto de perfil.
  - `bio` (`String`): Biografia do usuário (entre 6 e 100 caracteres).
  - `role` (`Role`): Papel do usuário (`USER` ou `ADMIN`).
  - `dataCriacao` (`LocalDate`): Data de cadastro gerada automaticamente via `@PrePersist`.
- **Relacionamentos:**
  - `listaJogos`: Lista de coleções criadas pelo usuário (`@OneToMany`).
  - `statusDosJogos`: Associação com o status dos jogos na biblioteca (`@OneToMany`, `CascadeType.ALL`, `orphanRemoval`).
  - `avaliacoes`: Avaliações feitas pelo usuário (`@OneToMany`).

### 3.2. `Jogo` (Tabela: `Jogos`)
Representa os jogos disponíveis no catálogo geral.
- **Atributos:**
  - `id` (`Long`): Identificador único.
  - `nome` (`String`): Título do jogo (obrigatório).
  - `capa` (`String`): URL da imagem de capa.
  - `plataformas` (`List<String>`): Plataformas suportadas (`@ElementCollection`).
  - `generos` (`List<String>`): Gêneros do jogo (`@ElementCollection`).
  - `desenvolvedora` (`String`): Empresa desenvolvedora.
  - `dataLancamento` (`LocalDate`): Data de lançamento.
  - `sinopse` (`String`): Descrição/sinopse do jogo.
  - `notaTotal` (`double`): Média consolidada das avaliações.
- **Relacionamentos:**
  - `listas`: Listas em que o jogo está incluído (`@ManyToMany`).
  - `statusPorUsuario`: Registros de status de biblioteca por usuário (`@OneToMany`).

### 3.3. `UsuarioJogo` (Tabela: `usuarios_jogos`)
Tabela associativa que gerencia a biblioteca pessoal e o progresso de cada usuário em relação a um jogo.
- **Atributos:**
  - `id` (`Long`): Identificador único.
  - `usuario` (`Usuario`): Usuário proprietário do vínculo (`@ManyToOne`).
  - `jogo` (`Jogo`): Jogo associado (`@ManyToOne`).
  - `status` (`StatusJogos`): Estado atual do jogo na biblioteca (`NAO_JOGADO`, `JOGANDO`, `QUERO_JOGAR`, `ZERADO`, `PLATINADO`, `ABANDONADO`).
- **Restrições:**
  - `uk_usuario_jogo`: Restrição de unicidade para evitar duplicação do mesmo jogo na biblioteca do mesmo usuário (`usuario_id` + `jogo_id`).

### 3.4. `Avaliacao` (Tabela: `avaliacoes`)
Armazena notas e comentários deixados por usuários em jogos.
- **Atributos:**
  - `id` (`Long`): Identificador único.
  - `usuario` (`Usuario`): Autor da avaliação (`@ManyToOne`).
  - `jogo` (`Jogo`): Jogo avaliado (`@ManyToOne`).
  - `notaEmMeiasEstrelas` (`int`): Nota de 0 a 10 (validada por `@Min(0)` e `@Max(10)`).
  - `comentario` (`String`): Comentário textual (entre 10 e 500 caracteres).
  - `dataCriacao` (`LocalDate`): Data da avaliação gerada via `@PrePersist`.

### 3.5. `ListaJogos` (Tabela: `ListasJogos`)
Permite aos usuários criarem listas temáticas ou customizadas de jogos.
- **Atributos:**
  - `id` (`Long`): Identificador único.
  - `usuario` (`Usuario`): Dono da lista (`@ManyToOne`).
  - `jogos` (`List<Jogo>`): Jogos pertencentes à lista (`@ManyToMany`, tabela `listas_jogos_jogos`).
  - `nome` (`String`): Nome da lista (obrigatório).
  - `publica` (`Boolean`): Visibilidade pública (`true`) ou privada (`false`).
  - `principal` (`boolean`): Identifica se é a biblioteca principal do usuário.

---

## 4. Métodos e Endpoints Detalhados

### 4.1. `UsuarioController` (`/usuarios`)
- **`POST /usuarios`** (`salvar`): Cadastra um novo usuário no sistema a partir de `UserCadastroRequest`. Retorna `UserResponse`.
- **`GET /usuarios`** (`listarTodos`): Retorna a lista de todos os usuários cadastrados (`List<UserResponse>`).
- **`GET /usuarios/{id}`** (`buscarUsuario`): Retorna os dados detalhados de um usuário específico pelo ID.
- **`PATCH /usuarios/{id}/Me`** (`atualizar`): Atualiza dados do perfil do usuário com base em `UserAtualizacaoRequest`.
- **`DELETE /usuarios/{id}`** (`deletar`): Remove um usuário do sistema pelo ID.
- **`POST /usuarios/login`** (`login`): Autentica o usuário a partir de `UserLoginRequest`, retornando `UserLoginResponse`.

### 4.2. `JogoController` (`/jogos`)
- **`POST /jogos`** (`criar`): Cadastra um novo jogo no catálogo geral.
- **`GET /jogos`** (`listarTodos`): Lista todos os jogos cadastrados, com suporte a filtro opcional por `nome` (`@RequestParam`).
- **`GET /jogos/{id}`** (`buscar`): Retorna os detalhes de um jogo específico.
- **`PUT /jogos/{id}`** (`atualizar`): Atualiza as informações de um jogo existente.
- **`DELETE /jogos/{id}`** (`deletar`): Remove um jogo do catálogo.

### 4.3. `AvaliacaoController` (`/avaliacoes`)
- **`POST /avaliacoes`** (`salvar`): Cria uma nova avaliação (nota e comentário) para um jogo (Status HTTP 201 Created).
- **`GET /avaliacoes/{id}`** (`buscar`): Busca uma avaliação específica pelo ID.
- **`GET /avaliacoes/usuario/{usuarioId}`** (`buscarPorUsuario`): Lista todas as avaliações feitas por um usuário.
- **`GET /avaliacoes/jogo/{jogoId}`** (`buscarPorJogo`): Lista todas as avaliações recebidas por um jogo.
- **`PUT /avaliacoes/{id}`** (`atualizar`): Atualiza o conteúdo ou nota de uma avaliação.
- **`DELETE /avaliacoes/{id}`** (`deletar`): Remove uma avaliação do sistema.

### 4.4. `ListaJogosController` (`/ListaJogos`)
- **`POST /ListaJogos`** (`salvar`): Cria uma nova lista personalizada de jogos para um usuário.
- **`PATCH /ListaJogos/{id}`** (`atualizar`): Atualiza os dados ou itens de uma lista de jogos.
- **`DELETE /ListaJogos/{id}`** (`deletar`): Remove uma lista de jogos pelo ID.

### 4.5. `MetricasSistemaController` (`/metricas`)
- **`GET /metricas`** (`listarTodos`): Retorna métricas gerais do sistema (`List<MetricaStatusResponse>`), utilizadas pelo painel do administrador.

---

## 5. Estrutura do Projeto (`com.gamebox.app`)

```text
com.gamebox.app
├── Config         # Configurações de CORS, WebMvc, etc.
├── Controller     # Endpoints REST (Usuario, Jogo, Avaliacao, ListaJogos, MetricasSistema)
├── Domain         # Entidades JPA (Usuario, Jogo, UsuarioJogo, Avaliacao, ListaJogos)
├── Dto            # Objetos de Transferência de Dados (Request / Response)
├── Enum           # Enumerações (Role, StatusJogos)
├── Repository     # Interfaces de Persistência (JpaRepository)
├── Security       # Configuração de segurança e criptografia
└── Service        # Regras de negócio e serviços da aplicação
```

---

## 6. Repositórios e Serviços

- **Repositórios (`JpaRepository`):** Fornecem métodos prontos de CRUD (`save`, `findById`, `findAll`, `deleteById`) além de consultas derivadas integradas ao Spring Data JPA.
- **Serviços:** Contêm a lógica transacional (`@Transactional`), validações de integridade, criptografia de senhas com `BCryptPasswordEncoder`, e recálculo dinâmico de notas médias de jogos.

---

## 7. Funcionalidades Planejadas (Data Loader)

- **Data Loader para Banco H2:** Funcionalidade planejada para realizar a carga inicial automática de dados de teste (contas de administradores, usuários comuns e jogos do catálogo) ao iniciar a aplicação. Tem como objetivo agilizar o desenvolvimento front-end e os testes de integração sem exigir cadastros manuais repetitivos. *(Nota: Esta funcionalidade está planejada e será implementada nas próximas etapas).*

---

## 8. Integrantes e Responsabilidades (Distribuição Inicial)

*Nota: Distribuição preliminar sujeita a revisões antes da entrega final.*

- **Luciano:** Backend (Pacotes `domain` e `repository`, desenvolvimento e manutenção, integração Java-Angular).
- **Luiz:** Backend (Desenvolvimento e manutenção do backend, serviços e regras de negócio).
- **Laura:** Banco de dados (Modelagem, persistência, configuração H2/JPA).
- **Michel:** Front-end (Desenvolvimento Angular, interfaces e consumo da API).
- **Rafael:** Front-end (Desenvolvimento Angular, componentes e navegação).
