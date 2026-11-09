# GameBox — Contrato de Integração Front-end ↔ Back-end

> Guia de implementação para o front-end Angular, baseado nos DTOs, controllers, configuração de segurança e configuração CORS existentes no repositório. Quando este documento divergir de guias anteriores, prevalece o código atual do backend. Revise este contrato junto com a equipe sempre que os endpoints ou DTOs mudarem.

## 1. Objetivo e visão geral

O GameBox é uma aplicação para consulta de catálogo de jogos, gestão de perfil e biblioteca, avaliações, listas e métricas. O front-end deve tratar o backend como fonte de verdade: enviar os contratos descritos aqui, consumir as respostas sem presumir campos extras e refletir erros e estados de carregamento na interface.

O backend está em `gamebox/` e usa Java/Spring Boot, Spring Web, Spring Data JPA, Spring Security e H2 em memória. A API REST local usa, por padrão, `http://localhost:8080`. O Angular local é esperado em `http://localhost:4200`.

### Subir e conferir a API

Na pasta `gamebox`, execute `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). Confirme os contratos publicados em `http://localhost:8080/swagger-ui/index.html` e os esquemas OpenAPI em `/v3/api-docs`. O console H2 está configurado em `/h2-console`. O banco é em memória: os dados não devem ser considerados persistentes entre reinicializações.

## 2. Regras para sincronizar os contratos

- Use os nomes JSON exatamente como aparecem abaixo: são os nomes dos campos dos records Java, em camelCase.
- IDs são números (`Long` no Java; `number` no TypeScript). Datas `LocalDate` são strings ISO `YYYY-MM-DD`, sem horário.
- Listas de resposta são arrays JSON; não há paginação implementada nos endpoints documentados.
- Envie JSON com `Content-Type: application/json` nos endpoints com corpo.
- Não envie entidades JPA completas por conveniência. Use os campos dos DTOs de request descritos neste guia.
- `senha` só é enviada em cadastro, login e atualização. A senha nunca deve ser armazenada no front-end nem exibida; o backend armazena hash BCrypt e não a devolve.
- `notaEmMeiasEstrelas` é um inteiro de 0 a 10: cada unidade equivale a meia estrela (0–5 estrelas). Não envie `4.5` para representar quatro estrelas e meia; envie `9`.
- A API não tem prefixo `/api` nos controllers atuais.
- A rota das listas está escrita com maiúsculas como `/ListaJogos`. Use essa grafia exatamente; não normalize para `/lista-jogos`.

## 3. Endpoints disponíveis

### 3.1 Usuários — `/usuarios`

| Método e rota | Uso | Corpo / resposta |
|---|---|---|
| `POST /usuarios` | Cadastrar | Corpo `UserCadastroRequest`; retorna `UserResponse` com `200 OK`. |
| `POST /usuarios/login` | Validar credenciais | Corpo `UserLoginRequest`; retorna `UserLoginResponse` com `200 OK`. |
| `GET /usuarios` | Listar usuários | Retorna `UserResponse[]`. |
| `GET /usuarios/{id}` | Buscar usuário | Retorna `UserResponse`; usuário inexistente resulta em `404`. |
| `PATCH /usuarios/{id}/Me` | Atualizar perfil | Corpo completo `UserAtualizacaoRequest`; retorna `UserResponse`. O `Me` tem M maiúsculo. |
| `DELETE /usuarios/{id}` | Excluir usuário | Sem corpo de resposta (`204 No Content`). |

`UserCadastroRequest`:
```json
{
  "nome": "Nome Sobrenome",
  "email": "pessoa@example.com",
  "senha": "senha123",
  "fotoPerfil": "https://example.com/avatar.png",
  "bio": "Minha bio"
}
```
`fotoPerfil` e `bio` podem ser omitidos ou nulos no cadastro. `nome` é obrigatório e tem mínimo de 5 caracteres; `email` é obrigatório e deve ter formato válido; `senha` é obrigatória e tem mínimo de 6 caracteres. E-mail duplicado retorna conflito (`409`).

`UserLoginRequest` é `{ "email": "pessoa@example.com", "senha": "senha123" }`. Sucesso retorna somente `{ "id": 1, "nome": "Nome Sobrenome", "email": "pessoa@example.com" }`. **Não existe token de autenticação nesta resposta.** Credenciais inválidas resultam em `409` com mensagem de erro.

`UserAtualizacaoRequest` exige `nome`, `email` e `senha`, mesmo sendo uma rota `PATCH`; não trate o corpo como parcial. `nome` mínimo 5, `email` válido, `senha` mínimo 6, `bio` opcional (quando informada, 6–100 caracteres) e `fotoPerfil` opcional. Atualizar o perfil também redefine a senha, então a tela deve solicitar e enviar a senha de acordo com o fluxo de produto definido pela equipe.

Resposta `UserResponse`:
```json
{
  "id": 1,
  "nome": "Nome Sobrenome",
  "email": "pessoa@example.com",
  "fotoPerfil": "https://example.com/avatar.png",
  "bio": "Minha bio",
  "dataCriacao": "2026-11-09"
}
```
A resposta não inclui `senha`, `senhaHash` nem `role`. O front-end não deve presumir que o papel está disponível.

### 3.2 Catálogo — `/jogos`

| Método e rota | Uso | Corpo / resposta |
|---|---|---|
| `GET /jogos` | Listar catálogo | Retorna `JogoResponse[]`. |
| `GET /jogos?nome={texto}` | Buscar por nome | Mesmo formato de resposta; parâmetro opcional `nome`. |
| `GET /jogos/{id}` | Detalhar jogo | Retorna `JogoResponse`; inexistente resulta em `404`. |
| `POST /jogos` | Cadastrar jogo | Corpo `JogoRequest`; retorna `JogoResponse`. |
| `PUT /jogos/{id}` | Substituir/atualizar dados | Corpo `JogoRequest`; retorna `JogoResponse`. |
| `DELETE /jogos/{id}` | Remover do catálogo | Sem corpo de resposta declarado pelo controller. |

`JogoRequest` (todos os campos abaixo são obrigatórios):
```json
{
  "nome": "Jogo de exemplo",
  "capa": "https://example.com/capa.jpg",
  "plataformas": ["PC", "PlayStation 5"],
  "generos": ["Ação", "Aventura"],
  "desenvolvedora": "Estúdio",
  "dataLancamento": "2024-05-10",
  "sinopse": "Descrição do jogo."
}
```
`nome`, `capa`, `desenvolvedora` e `sinopse` não podem ser vazios; `plataformas` e `generos` devem conter ao menos um item; `dataLancamento` deve ser uma data ISO válida.

`JogoResponse` contém `id`, `nome`, `capa`, `plataformas: string[]`, `generos: string[]`, `desenvolvedora`, `dataLancamento`, `sinopse` e `notaTotal: number`. `notaTotal` é calculada/gerenciada pelo backend; não a envie no request de jogo.

### 3.3 Avaliações — `/avaliacoes`

| Método e rota | Uso | Corpo / resposta |
|---|---|---|
| `POST /avaliacoes` | Criar avaliação | Corpo `AvaliacaoRequest`; `201 Created` com `AvaliacaoResponse`. |
| `GET /avaliacoes/{id}` | Buscar avaliação | Retorna `AvaliacaoResponse`; inexistente resulta em `404`. |
| `GET /avaliacoes/usuario/{usuarioId}` | Avaliações de um usuário | Retorna `AvaliacaoResponse[]`. |
| `GET /avaliacoes/jogo/{jogoId}` | Avaliações de um jogo | Retorna `AvaliacaoResponse[]`. |
| `PUT /avaliacoes/{id}` | Atualizar avaliação | Corpo `AvaliacaoAtualizacaoRequest`; retorna `AvaliacaoResponse`. |
| `DELETE /avaliacoes/{id}` | Excluir avaliação | Sem corpo de resposta (`204 No Content`). |

Criar requer os IDs de usuário e jogo, nota inteira de 0 a 10 e comentário de 10 a 500 caracteres:
```json
{
  "usuarioId": 1,
  "jogoId": 2,
  "notaEmMeiasEstrelas": 9,
  "comentario": "Gostei bastante deste jogo."
}
```
Atualização recebe apenas `notaEmMeiasEstrelas` e `comentario` (ambos obrigatórios). A resposta contém `id`, `usuarioId`, `jogoId`, `notaEmMeiasEstrelas`, `comentario` e `dataCriacao`.

### 3.4 Biblioteca e progresso — `/usuarios/{usuarioId}/jogos/{jogoId}`

Este módulo está implementado no controller, embora não apareça nos guias antigos de endpoints.

| Método e rota | Uso | Corpo / resposta |
|---|---|---|
| `POST /usuarios/{usuarioId}/jogos/{jogoId}` | Adicionar jogo à biblioteca | Sem corpo; `201 Created` com `UsuarioJogoResponse`. Se já existir, o vínculo existente é devolvido. |
| `GET /usuarios/{usuarioId}/jogos/{jogoId}` | Consultar vínculo/status | Retorna `UsuarioJogoResponse`; vínculo inexistente resulta em `404`. |
| `PATCH /usuarios/{usuarioId}/jogos/{jogoId}/status` | Atualizar progresso | Corpo `{ "status": "JOGANDO" }`; retorna `UsuarioJogoResponse`. |

Os valores aceitos para `status` são exatamente: `NAO_JOGADO`, `JOGANDO`, `QUERO_JOGAR`, `ZERADO`, `PLATINADO` e `ABANDONADO`. A resposta é `{ "id": 10, "usuarioId": 1, "jogoId": 2, "status": "JOGANDO" }`.

O vínculo de usuário e jogo é único. A atualização de status pode criar o vínculo caso ainda não exista. Uma regra de negócio pode impedir voltar para `NAO_JOGADO` quando o usuário já avaliou o jogo; nesse caso o backend retorna `409` e uma mensagem explicativa.

**Limitação atual:** o controller não oferece endpoint para listar todos os jogos da biblioteca ou filtrar por status; embora existam métodos correspondentes na camada de serviço, eles não estão expostos por uma rota HTTP neste momento. O front-end não deve inventar uma rota para essa operação.

### 3.5 Listas — `/ListaJogos`

| Método e rota | Uso | Corpo / resposta |
|---|---|---|
| `POST /ListaJogos` | Criar lista | `ListaJogosRequest`; `201 Created` com `ListaJogosResponse`. |
| `GET /ListaJogos` | Listar todas | Retorna `ListaJogosResponse[]`. |
| `GET /ListaJogos/{id}` | Buscar lista | Retorna `ListaJogosResponse`; inexistente resulta em `404`. |
| `GET /ListaJogos/usuario/{usuarioId}` | Listar listas de usuário | Retorna `ListaJogosResponse[]`. |
| `PUT /ListaJogos/{id}` | Atualizar lista | Corpo `ListaJogosRequest` completo; retorna `ListaJogosResponse`. |
| `DELETE /ListaJogos/{id}` | Excluir lista | Sem corpo de resposta (`204 No Content`). |

O corpo de criação/atualização é `{ "nome": "Favoritos", "publica": true, "principal": false, "usuarioId": 1 }`. `nome`, `publica` e `usuarioId` são obrigatórios. A atualização atual é `PUT`, não `PATCH`.

A resposta contém `id`, `nome`, `publica`, `principal`, `usuarioId`, `usuarioNome` e `jogos: JogoResponse[]`.

**Limitação atual:** `ListaJogosRequest` não recebe IDs de jogos e não há endpoint para adicionar/remover jogos da lista. Portanto, o front-end pode criar, consultar, atualizar metadados e excluir listas, mas não deve apresentar o gerenciamento de itens como funcionalidade integrada até o backend expor essa operação.

### 3.6 Métricas — `/metricas`

`GET /metricas` retorna `MetricaStatusResponse[]`, cada item no formato `{ "status": "JOGANDO", "quantidade": 3 }`. A métrica representa quantidade de vínculos por status. A resposta não é um objeto agregado com totais separados.

## 4. Tipos TypeScript sugeridos

```typescript
export type StatusJogos =
  | 'NAO_JOGADO'
  | 'JOGANDO'
  | 'QUERO_JOGAR'
  | 'ZERADO'
  | 'PLATINADO'
  | 'ABANDONADO';

export interface UserCadastroRequest {
  nome: string;
  email: string;
  senha: string;
  fotoPerfil?: string | null;
  bio?: string | null;
}

export interface UserLoginRequest {
  email: string;
  senha: string;
}

export interface UserAtualizacaoRequest {
  nome: string;
  email: string;
  senha: string;
  fotoPerfil?: string | null;
  bio?: string | null;
}

export interface UserResponse {
  id: number;
  nome: string;
  email: string;
  fotoPerfil: string | null;
  bio: string | null;
  dataCriacao: string;
}

export interface UserLoginResponse {
  id: number;
  nome: string;
  email: string;
}

export interface JogoRequest {
  nome: string;
  capa: string;
  plataformas: string[];
  generos: string[];
  desenvolvedora: string;
  dataLancamento: string;
  sinopse: string;
}

export interface JogoResponse extends JogoRequest {
  id: number;
  notaTotal: number;
}

export interface AvaliacaoRequest {
  usuarioId: number;
  jogoId: number;
  notaEmMeiasEstrelas: number;
  comentario: string;
}

export interface AvaliacaoAtualizacaoRequest {
  notaEmMeiasEstrelas: number;
  comentario: string;
}

export interface AvaliacaoResponse extends AvaliacaoAtualizacaoRequest {
  id: number;
  usuarioId: number;
  jogoId: number;
  dataCriacao: string;
}

export interface StatusUsuarioJogoRequest {
  status: StatusJogos;
}

export interface UsuarioJogoResponse {
  id: number;
  usuarioId: number;
  jogoId: number;
  status: StatusJogos;
}

export interface ListaJogosRequest {
  nome: string;
  publica: boolean;
  principal: boolean;
  usuarioId: number;
}

export interface ListaJogosResponse extends ListaJogosRequest {
  id: number;
  usuarioNome: string;
  jogos: JogoResponse[];
}

export interface MetricaStatusResponse {
  status: StatusJogos;
  quantidade: number;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}
```

> Em TypeScript, considerar campos opcionais ou anuláveis na resposta conforme os dados reais e a configuração de serialização. Os tipos acima são referência de integração; não substituem a validação do formulário nem a inspeção do OpenAPI.

## 5. Organização Angular recomendada

Centralize a URL da API por ambiente, em vez de espalhar `localhost` pelos componentes:

```typescript
// environment.development.ts
export const environment = {
  apiUrl: 'http://localhost:8080'
};
```

Crie serviços separados por domínio (`jogo.service.ts`, `usuario.service.ts`, `avaliacao.service.ts`, `biblioteca.service.ts`, `lista-jogos.service.ts` e `metricas.service.ts`). Os componentes devem chamar serviços e trabalhar com seus tipos, sem montar URLs ou JSON de forma duplicada.

Exemplo de serviço de jogos:
```typescript
@Injectable({ providedIn: 'root' })
export class JogoService {
  private readonly url = `${environment.apiUrl}/jogos`;

  constructor(private readonly http: HttpClient) {}

  listar(nome?: string): Observable<JogoResponse[]> {
    const params = nome ? new HttpParams().set('nome', nome) : undefined;
    return this.http.get<JogoResponse[]>(this.url, { params });
  }

  buscar(id: number): Observable<JogoResponse> {
    return this.http.get<JogoResponse>(`${this.url}/${id}`);
  }

  criar(request: JogoRequest): Observable<JogoResponse> {
    return this.http.post<JogoResponse>(this.url, request);
  }

  atualizar(id: number, request: JogoRequest): Observable<JogoResponse> {
    return this.http.put<JogoResponse>(`${this.url}/${id}`, request);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
```

Habilite `HttpClient` conforme a configuração da versão Angular usada (`provideHttpClient()` em aplicações standalone ou `HttpClientModule` em aplicações baseadas em módulos).

## 6. CORS, autenticação e limites de segurança atuais

- O CORS do backend permite a origem literal `http://localhost:4200`, todos os métodos e todos os cabeçalhos. Outra origem/porta (incluindo IP local, HTTPS ou porta diferente) exige ajuste explícito no backend.
- A configuração Spring Security atual desativa CSRF e usa `permitAll()` para todas as rotas. Isso significa que hoje o backend não exige autenticação/autorização nas chamadas, inclusive operações administrativas.
- O login valida email e senha e devolve dados do usuário, mas não gera JWT, cookie de sessão nem token. Não implemente interceptor `Bearer` com base em uma suposição de token inexistente.
- A rota `/usuarios/{id}/Me` contém `id` no caminho; a configuração atual não garante que o usuário só possa editar a própria conta. Não trate o front-end como barreira de segurança.
- Não exponha credenciais, hash ou dados sensíveis no estado da aplicação ou logs do navegador. A proteção de autorização deve ser definida e implementada no backend antes de uso público/produção.

## 7. Erros e validação no front-end

Os erros tratados globalmente usam este formato JSON:
```json
{
  "timestamp": "2026-11-09T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "nome: O nome deve ter pelo menos 5 caracteres"
}
```

Status atualmente relevantes:

- `400 Bad Request`: validação de DTO, JSON/datas inválidos ou parâmetro inválido.
- `404 Not Found`: recurso ou vínculo não encontrado.
- `409 Conflict`: regra de negócio, como email duplicado, credenciais inválidas ou transição de status não permitida.
- `201 Created`: criação de avaliação, lista ou vínculo de biblioteca.
- `204 No Content`: exclusões sem representação de resposta.

O formato global foi verificado no handler de exceções. Erros não tratados e erros de rede podem ter outro formato. No Angular, leia `HttpErrorResponse.error?.message` com fallback para uma mensagem genérica; em `204`, não tente acessar propriedades do body. Evite mostrar mensagens técnicas diretamente sem contexto e preserve os valores preenchidos em caso de falha.

## 8. Fluxos sugeridos de interface

1. **Catálogo:** chamar `GET /jogos`; usar `?nome=` para busca; abrir detalhe com `GET /jogos/{id}`.
2. **Cadastro/login:** enviar DTOs correspondentes; após login, guardar somente o mínimo necessário para a experiência de navegação. Como não há sessão/token no backend, não considerar o estado local uma autorização.
3. **Biblioteca:** adicionar com `POST /usuarios/{usuarioId}/jogos/{jogoId}`; consultar cada vínculo com `GET` e atualizar com `PATCH .../status`. Para listar toda a biblioteca, alinhar antes a criação de um endpoint com backend.
4. **Avaliação:** enviar `usuarioId`, `jogoId`, nota inteira de 0–10 e comentário válido; atualizar usando `PUT` e os campos de avaliação.
5. **Listas:** criar e editar metadados com os DTOs disponíveis. Não oferecer associação de jogos enquanto não houver contrato de backend para isso.
6. **Métricas:** consumir array de status/quantidade e tratar status como enum; não presumir categorias ou totais ausentes na resposta.

Para telas com dados remotos, implemente estados distintos de carregamento, vazio, sucesso e erro. Recarregue o recurso afetado após mutações ou atualize o estado local usando a resposta retornada pelo backend.

## 9. Diferenças importantes em relação aos guias anteriores

O código atual é mais completo em alguns pontos e diverge dos documentos antigos:

- Listas têm `GET /ListaJogos`, `GET /ListaJogos/{id}` e `GET /ListaJogos/usuario/{usuarioId}`; atualização é `PUT`, não `PATCH`.
- Existe o controller de biblioteca `UsuarioJogoController` com rotas POST, GET e PATCH de status, ausente nos guias anteriores.
- A rota para criar avaliação usa `AvaliacaoRequest` com IDs e `notaEmMeiasEstrelas`; não enviar objetos de entidade `Avaliacao`.
- Cadastro de usuário retorna `200 OK` (não `201`); criação de lista retorna `201 Created` (não `200`).
- Login retorna `{id, nome, email}` e não retorna token.
- `UserResponse` não tem `role`.
- Atualização do usuário exige senha no DTO mesmo sendo `PATCH`.
- Listas não aceitam jogos no request atual e biblioteca não tem endpoint HTTP para listagem.
- Existe código de DataLoader no repositório; a presença de classes de carga não garante, por si só, quais dados estarão disponíveis em cada ambiente. Não fixe IDs ou credenciais de teste no front-end; confirme os dados em execução.

## 10. Checklist para cada alteração de integração

- [ ] Conferir rota, método HTTP, capitalização e DTO no controller/Swagger.
- [ ] Atualizar interfaces TypeScript e formulários na mesma alteração de contrato.
- [ ] Validar datas ISO, enums, limites de tamanho e IDs numéricos.
- [ ] Tratar `400`, `404`, `409`, `201` e `204` corretamente.
- [ ] Confirmar CORS com a origem real do front-end.
- [ ] Não supor autenticação/token, campos ocultos ou endpoints que não estejam publicados.
- [ ] Testar o fluxo integrado com o backend em execução e atualizar este documento se o contrato mudar.

---

**Fontes do contrato:** `gamebox/src/main/java/com/gamebox/app/Controller`, `Dto/request`, `Dto/response`, `Config/CorsConfig.java`, `Security/SecurityConfig.java`, `Exception/TratamentoGlobalException.java` e `src/main/resources/application.properties`.
