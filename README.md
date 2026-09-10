# ChupinVet API

A API ChupinVet atua como camada de comunicação entre o aplicativo mobile do projeto 
(React Native + Expo) e o banco de dados Oracle, permitindo que todas as informações 
cadastradas no sistema sejam acessadas, atualizadas e gerenciadas de forma segura.

Através da API, o aplicativo mobile realiza operações de cadastro, 
consulta, atualização e remoção de dados relacionados a **pets**, **responsáveis**, **veterinários** e **registros de diário**, 
com autenticação via **JWT** e permissões diferentes para cada tipo de usuário.

A API centraliza as regras de negócio da aplicação validações, 
autenticação, autorização por papel e por posse (um responsável só acessa os próprios dados) 
garantindo que essas regras sejam aplicadas de forma padronizada, 
independente de qual cliente está consumindo a API.

---

# Integrantes

| Nome | RM | Turma |
|---|---|---|
| Agatha Yie Won Yun | RM561507 | 2TDSA |
| Ana Claudia Fernandes Martins | RM561190 | 2TDSR |
| Samantha Faruolo Galdi | RM554794 | 2TDSA |
| Vitor Fria Dalmagro | RM566052 | 2TDSA |

---

# Tecnologias Utilizadas

- Java 25
- Spring Boot 4.0.6 (Spring Framework 7 / Spring Security 7)
- Spring Data JPA + Hibernate ORM
- Spring Security + JWT (io.jsonwebtoken / jjwt 0.13)
- Oracle Database (driver ojdbc11)
- Maven
- Springdoc OpenAPI (Swagger UI, com suporte a Bearer Token)
- Lombok
- Bean Validation (Jakarta Validation)
- BCrypt (hash de senhas)

---

# Arquitetura do Projeto

## Explicando cada camada

### Controller
Endpoints REST da aplicação. Também é onde ficam as anotações `@PreAuthorize`, 
que restringem cada rota por papel (`ROLE_RESPONSAVEL` / `ROLE_VETERINARIO`).

### Service
Regras de negócio, incluindo as checagens de **posse** (exemplo: um responsável só edita/deleta os próprios pets), 
feitas via `SecurityUtils`.

### Repository
Comunicação com o banco via Spring Data JPA (Query Methods).

### DTO
Transferência de dados entre cliente e servidor, 
separa o que é aceito na entrada (`*RequestDTO`) do que é exposto na saída (`*ResponseDTO`).

### Model
Entidades JPA mapeadas para o schema Oracle.

### Exception
Tratamento centralizado de erros (`GlobalExceptionHandler`), 
convertendo exceções em respostas HTTP (400, 401, 403, 404, 409, 500).

### Security
Toda a infraestrutura de autenticação/autorização: geração e validação de JWT, 
filtro de autenticação por requisição, e a configuração de quais rotas são públicas ou protegidas.

### Config
Configurações gerais da aplicação (exemplo: integração do Swagger com autenticação Bearer).

---

# Estrutura do Projeto

```txt
src/main/java/br/com/chupinvet/chupinvet
│
├── config           => Configurações gerais (Swagger/OpenAPI com Bearer Auth)
├── controller       => Endpoints REST
├── dto              => Objetos de transferência de dados
├── exception        => Exceções customizadas e handler global
├── model            => Entidades JPA
├── repository       => Interfaces Spring Data JPA
└── service
    └── insight      => Ponto de extensão para geração de insights (IA, parte do plano de melhoria)
```

---

# Banco de Dados

O projeto utiliza **Oracle Database**. O schema é compartilhado com uma API .NET do mesmo projeto 
(responsável por Consulta, Histórico de Consulta e Prontuário), por isso o Hibernate está configurado com 
`ddl-auto=validate`: a aplicação **nunca** altera o schema sozinha, apenas confere se as entidades batem com as tabelas reais.

## Principais entidades

- **Usuario** — identidade base (nome, e-mail, senha, CPF, telefone, etc.)
- **Responsavel** — dados específicos de quem é responsável por pets
- **Veterinario** — dados específicos de profissionais veterinários
- **Pet** — animais cadastrados, vinculados a um responsável
- **Diario** — registros diários de saúde/comportamento de um pet, feitos pelo responsável

## Relacionamentos

- `Responsavel` e `Veterinario` **não** herdam de `Usuario`, 
cada um tem sua própria chave primária e se associa a um `Usuario` por uma relação `@OneToOne` (composição), refletindo o modelo físico do banco.
- Um `Responsavel` possui vários `Pet` (1:N).
- Um `Pet` possui vários `Diario` (1:N).
- Um `Usuario` é um `Responsavel` **ou** um `Veterinario`, o papel é derivado dinamicamente (não existe uma coluna "tipo" no banco), verificando em qual tabela existe um registro associado.

---

# Autenticação e Autorização

A API usa **JWT** (Bearer Token). 
Não existe endpoint de "signup" separado, cadastrar um Responsável ou Veterinário (`POST /responsaveis` ou `POST /veterinarios`) 
já cria a conta de login.

## Fluxo básico

1. `POST /responsaveis` **ou** `POST /veterinarios`, cadastro (rota pública)
2. `POST /auth/login` — envia e-mail e senha, recebe um token JWT
3. Envia o token em todas as demais requisições: header `Authorization: Bearer <tokenJwt>`

## Papéis e permissões

| Papel | Pode fazer |
|---|---|
| `ROLE_RESPONSAVEL` | CRUD dos próprios pets e diários; CRUD do próprio cadastro |
| `ROLE_VETERINARIO` | Listar todos os pets e responsáveis (leitura), CRUD do próprio cadastro de veterinário |

Além da checagem por papel, existe checagem de **posse**: 
um Responsável não consegue ver, editar ou deletar pets, 
diários ou o cadastro de **outro** Responsável, 
mesmo tendo um token válido, tentativas assim retornam `403`.

## Rotas públicas (não exigem token)

- `POST /responsaveis`
- `POST /veterinarios`
- `POST /auth/login`
- `/swagger-ui.html`

Todas as demais rotas exigem token válido.

---

# Funcionalidades Implementadas

- CRUD completo de Pets, Responsáveis, Veterinários e Diário
- Autenticação via JWT (login) + autorização por papel (`@PreAuthorize`)
- Autorização por posse (um usuário só gerencia os próprios dados)
- Senhas armazenadas com hash (BCrypt), nunca em texto puro
- Paginação e ordenação de resultados
- Busca por parâmetros (nome, espécie, raça, especialidade, tipo de serviço)
- Bean Validation, com retorno `400` detalhando o campo inválido
- Tratamento de exceções centralizado (`400`, `401`, `403`, `404`, `409`, `500`)
- Documentação via Swagger/OpenAPI, com suporte a autenticação Bearer direto na interface
- Relacionamentos JPA mapeados via composição (`@OneToOne`/`@ManyToOne`/`@OneToMany`)
- Ponto de extensão (`InsightProvider`) preparado para integração futura com uma API de IA externa, 
que vai gerar insights automáticos nos registros de diário

---

# Como Executar o Projeto

## 1. Clonar o repositório

```bash
git clone https://github.com/ChupinVet/JavaChallenge.git
cd JavaChallenge
```

## 2. Configurar variáveis de ambiente

O projeto se conecta ao Oracle compartilhado da FIAP. 
**Usuário e senha não têm valor padrão no código** 
(o repositório é público), são obrigatórios via variável de ambiente:

| Variável | Obrigatória? | Descrição |
|---|---|---|
| `DB_USER` | Sim | Usuário do Oracle (ex.: `seuusuario`) |
| `DB_PASSWORD` | Sim | Senha do Oracle |
| `DB_URL` | Não | Padrão: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` |
| `JWT_SECRET` | Não em dev (tem valor padrão só para desenvolvimento) / **Sim em produção** | Chave de assinatura do JWT, 32+ caracteres |
| `JWT_EXPIRATION_MS` | Não | Tempo de expiração do token em milissegundos (padrão: 3600000 = 1h) |

Exemplo (Linux/macOS):
```bash
export DB_USER=seuusuario
export DB_PASSWORD=suasenha
```

No Windows (PowerShell):
```powershell
$env:DB_USER="seuusuario"
$env:DB_PASSWORD="suasenha"
```

Ou configure essas variáveis direto no Run Configuration da sua IDE (Eclipse, IntelliJ).

## 3. Executar o projeto

```bash
./mvnw spring-boot:run
```

ou:

```bash
mvn spring-boot:run
```

Por padrão, a aplicação sobe com o profile `dev` (`application-dev.properties`). 
Para produção, o profile `prod` é ativado automaticamente pela variável `SPRING_PROFILES_ACTIVE=prod` 
(configurada no Render).

---

# Acesso à Aplicação

## URL base (produção)

```txt
https://javachallenge.onrender.com
```

## URL base (local)

```txt
http://localhost:8080
```

> O Render (plano gratuito) "dorme" após 15 minutos sem uso, 
a primeira requisição depois disso pode demorar alguns segundos para responder enquanto o serviço acorda.

---

# Swagger

Depois de iniciar a aplicação, acesse:

```txt
http://localhost:8080/swagger-ui/index.html
```
ou, em produção:
```txt
https://javachallenge.onrender.com/swagger-ui/index.html
```

## Testando endpoints protegidos pelo Swagger

1. Cadastre um usuário via `POST /responsaveis` ou `POST /veterinarios`
2. Faça login via `POST /auth/login` e copie o valor de `token` da resposta
3. Clique no botão **Authorize** (cadeado, no topo da página) e 
cole o token (sem o prefixo `Bearer`, o Swagger adiciona sozinho)
4. Os endpoints protegidos passam a funcionar normalmente

---

# Endpoints da API

## Autenticação

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/auth/login` | Login (retorna token JWT) | Público |

## Responsáveis

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/responsaveis` | Cadastra um novo responsável (signup) | Público |
| GET | `/responsaveis` | Lista todos os responsáveis | Veterinário |
| GET | `/responsaveis/{id}` | Busca responsável por ID | Veterinário, ou o próprio responsável |
| GET | `/responsaveis?page=0&size=10&sort=nomeUsuario,asc` | Busca com paginação/ordenação | Veterinário |
| PUT | `/responsaveis/{id}` | Atualiza um responsável | O próprio responsável |
| DELETE | `/responsaveis/{id}` | Remove um responsável | O próprio responsável |

## Veterinários

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/veterinarios` | Cadastra um novo veterinário (signup) | Público |
| GET | `/veterinarios` | Lista todos os veterinários | Autenticado |
| GET | `/veterinarios/{id}` | Busca veterinário por ID | Autenticado |
| GET | `/veterinarios?page=0&size=10&sort=nomeUsuario,asc` | Busca com paginação/ordenação | Autenticado |
| GET | `/veterinarios/especialidade?especialidade=Cirurgia` | Busca por especialidade | Autenticado |
| GET | `/veterinarios/servico?tipoServico=Consulta` | Busca por tipo de serviço | Autenticado |
| PUT | `/veterinarios/{id}` | Atualiza um veterinário | O próprio veterinário |
| DELETE | `/veterinarios/{id}` | Remove um veterinário | O próprio veterinário |

## Pets

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/pets` | Cadastra um novo pet (dono = responsável autenticado) | Responsável |
| GET | `/pets` | Lista pets (responsável vê só os seus; veterinário vê todos) | Autenticado |
| GET | `/pets/{id}` | Busca pet por ID | Autenticado (com checagem de posse) |
| GET | `/pets?page=0&size=10&sort=nomePet,asc` | Busca com paginação/ordenação | Autenticado |
| GET | `/pets/nome?nomePet=Thor` | Busca pets por nome | Autenticado |
| GET | `/pets/especie?especie=Cachorro` | Busca pets por espécie | Autenticado |
| GET | `/pets/raca?raca=Pug` | Busca pets por raça | Autenticado |
| PUT | `/pets/{id}` | Atualiza um pet | O responsável dono do pet |
| DELETE | `/pets/{id}` | Remove um pet | O responsável dono do pet |

## Diário

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/diarios` | Cria um registro de diário para um pet | Responsável dono do pet |
| GET | `/diarios/pet/{idPet}` | Lista o histórico de diário de um pet (mais recente primeiro) | Autenticado (com checagem de posse) |
| GET | `/diarios/{id}` | Busca um registro específico | Autenticado (com checagem de posse) |
| PUT | `/diarios/{id}` | Atualiza um registro | O responsável dono do pet |
| DELETE | `/diarios/{id}` | Remove um registro | O responsável dono do pet |

---

# Exemplos de Busca

```txt
GET /pets/nome?nomePet=Thor
GET /veterinarios/especialidade?especialidade=Cardiologia
GET /diarios/pet/1?page=0&size=5
```

---

# Testando a API

Uma coleção Postman (compatível com Insomnia) com todos os fluxos cadastro,
login, CRUD completo e os casos de erro (`400`, `401`, `403`, `404`, `409`) 
está disponível em `chupinvet.postman_collection.json`, na raiz do repositório.

---

# Próximos Passos

- **Flyway**: fora de escopo nesta sprint, por decisão do professor. O schema é criado e mantido manualmente (`Estrutura_e_Carga.sql`), compartilhado com a API .NET do projeto.
- **Insight de IA no Diário**: o campo `insightIA` existe no modelo, mas não é preenchido nesta sprint — será gerado por uma API de IA externa, a ser integrada futuramente.

---

# Documentação da API

A documentação completa e interativa está disponível via Swagger UI.

---

# Disciplina

Java Advanced | Sprints 1, 2 e 3

FIAP
