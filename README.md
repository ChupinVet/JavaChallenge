#  ChupinVet API

A API ChupinVet atua como camada de comunicação entre o aplicativo mobile do projeto e o banco de dados Oracle, 
permitindo que todas as informações cadastradas no sistema sejam acessadas, 
atualizadas e gerenciadas.

Através da API, o aplicativo mobile consegue realizar operações de cadastro, 
consulta, atualização e remoção de dados relacionados a pets, responsáveis e veterinários, 
garantindo integração entre o front-end mobile e a base de dados da aplicação.

A utilização da API no projeto estão a 
centralização das regras de negócio da aplicação, permitindo que todas as 
operações e validações sejam realizadas de forma padronizada.

---

# Integrantes

| Nome | RM | Turma |
|---|---|---|       
|Agatha Yie Won Yun|RM561507|2TDSA| 
|Ana Claudia Fernandes Martins| RM561190| 2TDSR|   
|Samantha Faruolo Galdi| RM554794| 2TDSA|
|Vitor Fria Dalmagro| RM566052| 2TDSA|



---

# Tecnologias Utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA
- Hibernate
- Oracle Database
- Maven
- Swagger
- Lombok
- Bean Validation

---

# Arquitetura do Projeto

## Explicando cada Camada

### Controller
Responsável pelos endpoints da aplicação.

### Service
Responsável pelas regras de negócio da aplicação.

### Repository
Responsável pela comunicação com o banco de dados utilizando Spring Data JPA.

### DTO
Responsável pela transferência de dados entre cliente e servidor.

### Exception
Responsável pelo tratamento de exceções da aplicação.

---

#  Estrutura do Projeto

```txt
src/main/java/br/com/chupinvet/chupinvet
│
├── controller
├── dto
├── exception
├── model
├── repository
└── service
```

---

# Banco de Dados

O projeto utiliza Oracle Database.

## Principais entidades

- Usuario
- Responsavel
- Veterinario
- Pet

## Relacionamentos

- Um responsável pode possuir vários pets
- Um pet pertence a um responsável
- Responsável e Veterinário herdam de Usuario

---

#  Funcionalidades Implementadas

- CRUD completo de Pets  
- CRUD completo de Responsáveis  
- CRUD completo de Veterinários  
- Paginação de resultados  
- Ordenação  
- Busca por parâmetros  
- Bean Validation  
- Tratamento de exceções  
- Swagger/OpenAPI  
- Relacionamentos JPA  
- Herança com JOINED

---

# Como Executar o Projeto

## 1️ Clonar o repositório

```bash
git clone https://github.com/ChupinVet/JavaChallenge.git
```

---

## 2️ Entrar na pasta do projeto

```bash
cd JavaChallenge
```


---

## 3 Executar o projeto

```bash
./mvnw spring-boot:run
```

ou:

```bash
mvn spring-boot:run
```

---
# URL Base!
```txt
https://javachallenge.onrender.com
```
---
#  Swagger

Após iniciar a aplicação:

## Swagger UI

```txt
https://javachallenge.onrender.com/swagger-ui.html
```

---

# Endpoints da API


## Responsáveis

| Método | Endpoint                                             | Descrição                       |
|---|------------------------------------------------------|---------------------------------|
| POST | `/responsaveis`                                      | Cadastra um novo responsável    |
| GET | `/responsaveis`                                      | Lista todos os responsáveis     |
| GET | `/responsaveis/{id}`                                 | Busca responsável por ID        |
| GET | `/responsaveis?page=0&size=10&sort=nomeUsuario,asc ` | Busca responsável com paginação |
| PUT | `/responsaveis/{id}`                                 | Atualiza um responsável         |
| DELETE | `/responsaveis/{id}`                                 | Remove um responsável           |

---

## Pets

| Método | Endpoint | Descrição             |
|---|---|-----------------------|
| POST | `/pets` | Cadastra um novo pet  |
| GET | `/pets` | Lista todos os pets   |
| GET | `/pets/{id}` | Busca pet por ID      |
| GET | `/pets?page=0&size=10&sort=nomePet,asc` | Busca com paginação   |
| GET | `/pets/nome?nomePet=Thor` | Busca pets por nome   |
| GET | `/pets/especie?especie=Cachorro` | Busca pets por espécie |
| GET | `/pets/raca?raca=Pug` | Busca pets por raça   |
| PUT | `/pets/{id}` | Atualiza um pet       |
| DELETE | `/pets/{id}` | Remove um pet         |

---

## Veterinários

| Método | Endpoint | Descrição                              |
|---|---|----------------------------------------|
| POST | `/veterinarios` | Cadastra um novo veterinário           |
| GET | `/veterinarios` | Lista todos os veterinários            |
| GET | `/veterinarios/{id}` | Busca veterinário por ID               |
| GET | `/veterinarios?page=0&size=10&sort=nomeUsuario,asc` | Busca veterinário com paginação        |
| GET | `/veterinarios/especialidade?especialidade=Cirurgia` | Busca veterinários por especialidade   |
| GET | `/veterinarios/servico?tipoServico=Consulta` | Busca veterinários por tipo de serviço |
| PUT | `/veterinarios/{id}` | Atualiza um veterinário                |
| DELETE | `/veterinarios/{id}` | Remove um veterinário                  |

---

## Swagger

| Recurso | Endpoint |
|---|---|
| Swagger UI | `/swagger-ui.html` |

---

# Exemplos de Busca

## Buscar pets por nome

```txt
GET /pets/nome?nomePet=Thor
```

## Buscar veterinários por especialidade

```txt
GET /veterinarios/especialidade?especialidade=Cardiologia
```

---

#  Documentação da API

A documentação completa da API pode ser acessada através do Swagger UI.

---


#  Disciplina

Java Advanced — 1º e 2° Sprint Challenge

FIAP
