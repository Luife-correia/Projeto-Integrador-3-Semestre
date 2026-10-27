# Guia Prático de Integração Java (Spring Boot) + Angular — GameBox

Este manual foi elaborado para orientar a equipe de front-end na integração com o backend do projeto **GameBox**. Ele detalha os endpoints disponíveis, contratos de DTOs, configurações de CORS, Swagger e um roteiro prático para o consumo da API.

---

## 1. Estrutura do Backend

O backend segue a arquitetura em camadas padrão do Spring Boot:
- **`Controller`**: Recebe as requisições HTTP, valida os dados de entrada (`@Valid`) e retorna respostas via `ResponseEntity`.
- **`Service`**: Contém a regra de negócio e processamento.
- **`Repository`**: Comunicação com o banco de dados H2 via Spring Data JPA.
- **`Domain`**: Entidades mapeadas no banco de dados.
- **`Dto`**: Objetos de requisição (`Request`) e resposta (`Response`) que blindam as entidades de domínio.
- **`Enum`**: Definições fixas (`Role`, `StatusJogos`).

### Fluxo de uma Requisição HTTP:
1. O Front-end Angular faz uma requisição HTTP (ex: `POST /usuarios`) enviando um JSON.
2. O `UsuarioController` intercepta a chamada, valida o DTO (`UserCadastroRequest`) e aciona o `UsuarioService`.
3. O `UsuarioService` executa as regras de negócio (ex: criptografia de senha com `BCrypt`) e chama o `UsuarioRepository`.
4. O `UsuarioRepository` persiste no banco H2 e retorna a entidade.
5. O `UsuarioService` converte a entidade em `UserResponse` e devolve ao Controller, que responde com status HTTP adequado (ex: `200 OK` ou `201 Created`).

---

## 2. Endpoints Disponíveis

Abaixo estão listados todos os endpoints da API REST do GameBox organizados por módulo:

### 2.1. Usuários (`/usuarios`)
- **`POST /usuarios`**
  - **Finalidade:** Cadastrar um novo usuário.
  - **Payload (`UserCadastroRequest`):** `{ "nome": "...", "email": "...", "senha": "..." }`
  - **Resposta (`UserResponse`):** Dados do usuário cadastrado (sem senha).
  - **Status HTTP:** `200 OK` / `400 Bad Request`.
- **`POST /usuarios/login`**
  - **Finalidade:** Autenticar um usuário.
  - **Payload (`UserLoginRequest`):** `{ "email": "...", "senha": "..." }`
  - **Resposta (`UserLoginResponse`):** Token/dados de sessão do usuário.
  - **Status HTTP:** `200 OK` / `401 Unauthorized`.
- **`GET /usuarios`**
  - **Finalidade:** Listar todos os usuários cadastrados.
  - **Resposta:** `List<UserResponse>`.
  - **Status HTTP:** `200 OK`.
- **`GET /usuarios/{id}`**
  - **Finalidade:** Buscar um usuário específico pelo ID (`Path Variable`).
  - **Resposta:** `UserResponse`.
  - **Status HTTP:** `200 OK` / `404 Not Found`.
- **`PATCH /usuarios/{id}/Me`**
  - **Finalidade:** Atualizar dados do perfil do usuário (`UserAtualizacaoRequest`).
  - **Resposta:** `UserResponse`.
  - **Status HTTP:** `200 OK`.
- **`DELETE /usuarios/{id}`**
  - **Finalidade:** Excluir um usuário pelo ID.
  - **Status HTTP:** `204 No Content`.

### 2.2. Jogos / Catálogo (`/jogos`)
- **`GET /jogos`**
  - **Finalidade:** Listar todos os jogos do catálogo. Permite filtro por nome via Query Param (`?nome=...`).
  - **Resposta:** `List<Jogo>`.
  - **Status HTTP:** `200 OK`.
- **`GET /jogos/{id}`**
  - **Finalidade:** Buscar detalhes de um jogo específico.
  - **Resposta:** `Jogo`.
  - **Status HTTP:** `200 OK` / `404 Not Found`.
- **`POST /jogos`**
  - **Finalidade:** Cadastrar um novo jogo no catálogo.
  - **Payload:** Objeto `Jogo`.
  - **Status HTTP:** `200 OK` / `201 Created`.
- **`PUT /jogos/{id}`**
  - **Finalidade:** Atualizar informações de um jogo.
  - **Status HTTP:** `200 OK`.
- **`DELETE /jogos/{id}`**
  - **Finalidade:** Remover um jogo do catálogo.
  - **Status HTTP:** `200 OK` / `204 No Content`.

### 2.3. Avaliações (`/avaliacoes`)
- **`POST /avaliacoes`**
  - **Finalidade:** Criar uma avaliação para um jogo.
  - **Payload:** Objeto `Avaliacao`.
  - **Status HTTP:** `201 Created`.
- **`GET /avaliacoes/{id}`**
  - **Finalidade:** Buscar avaliação por ID.
- **`GET /avaliacoes/usuario/{usuarioId}`**
  - **Finalidade:** Listar avaliações feitas por um usuário específico.
- **`GET /avaliacoes/jogo/{jogoId}`**
  - **Finalidade:** Listar todas as avaliações recebidas por um jogo.
- **`PUT /avaliacoes/{id}`**
  - **Finalidade:** Atualizar uma avaliação existente.
- **`DELETE /avaliacoes/{id}`**
  - **Finalidade:** Excluir uma avaliação.

### 2.4. Listas de Jogos (`/ListaJogos`)
- **`POST /ListaJogos`**
  - **Finalidade:** Criar uma nova lista personalizada de jogos.
  - **Payload:** Objeto `ListaJogos`.
  - **Status HTTP:** `200 OK`.
- **`PATCH /ListaJogos/{id}`**
  - **Finalidade:** Atualizar uma lista existente.
  - **Status HTTP:** `200 OK`.
- **`DELETE /ListaJogos/{id}`**
  - **Finalidade:** Excluir uma lista de jogos.
  - **Status HTTP:** `204 No Content`.

### 2.5. Métricas do Sistema (`/metricas`)
- **`GET /metricas`**
  - **Finalidade:** Obter métricas gerais do sistema para o painel administrativo.
  - **Resposta:** `List<MetricaStatusResponse>`.
  - **Status HTTP:** `200 OK`.

---

## 3. Utilização do Swagger (OpenAPI)

O projeto possui integração com o **SpringDoc OpenAPI**. Para testar e inspecionar os endpoints de forma interativa:
1. Inicie a aplicação Spring Boot.
2. Acesse no navegador: `http://localhost:8080/swagger-ui/index.html`
3. Na interface do Swagger, você poderá visualizar todos os contratos, parâmetros exigidos, esquemas de DTOs e executar requisições de teste diretamente (botão "Try it out").

---

## 4. Integração com o Angular

### 4.1. Configuração de CORS
O backend já está configurado (`CorsConfig.java`) para aceitar requisições originadas em `http://localhost:4200` (porta padrão do Angular) com todos os métodos (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`) e cabeçalhos permitidos.

### 4.2. Exemplo de Serviço Angular (`HttpClient`)
Crie serviços dedicados no Angular utilizando o `HttpClient`:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserResponse {
  id: number;
  nome: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/usuarios';

  constructor(private http: HttpClient) {}

  listarUsuarios(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.apiUrl);
  }

  cadastrarUsuario(dados: any): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiUrl, dados);
  }
}
```

---

## 5. Banco H2 e Ambiente de Desenvolvimento

- A aplicação utiliza o banco de dados em memória **H2**.
- Para acessar o console do H2 durante a execução, acesse `http://localhost:8080/h2-console` (conforme configurações em `application.properties`).
- **Data Loader:** Funcionalidade planejada para carga automática de dados de teste na inicialização, simplificando os testes ponta a ponta.

---

## 6. Guia Prático / Roteiro para a Desenvolvedora Front-end

1. **Subir o Backend:** Execute a aplicação Spring Boot a partir da IDE ou via terminal (`mvn spring-boot:run` dentro da pasta `gamebox`).
2. **Explorar o Swagger:** Abra `http://localhost:8080/swagger-ui/index.html` para validar os payloads aceitos por cada endpoint.
3. **Mapear Interfaces TypeScript:** Crie interfaces no Angular equivalentes aos DTOs de Request e Response do backend.
4. **Criar Serviços Angular:** Implemente os serviços injetáveis com `HttpClient` apontando para `http://localhost:8080`.
5. **Conectar Componentes:** Integre os métodos dos serviços às telas de listagem de jogos, login, cadastro, perfil e avaliações.
