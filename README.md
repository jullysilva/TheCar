# TheCar

🇧🇷 Português | [🇺🇸 English](README.en.md)

Plataforma de aluguel de veículos ("Airbnb de carros") desenvolvida com **Spring Boot**. Oferece uma interface web com Thymeleaf para gerenciar o catálogo de carros e uma API REST protegida por **JWT**.

## Stack

- Java 21, Spring Boot, Maven
- Spring Web, Spring Data JPA, Spring Security
- JWT (jjwt)
- Thymeleaf + Thymeleaf Layout Dialect
- H2 Database (em memória)
- springdoc-openapi (Swagger UI)
- Lombok

## Estrutura

```
src/main/java/com/example/thecar/
├── config/       # Configuração do Swagger
├── controller/   # Controllers web (Thymeleaf) e REST
├── model/        # Entidades Car e User
├── repository/   # Repositórios JPA
└── security/     # Filtro JWT, serviço de token e SecurityConfig
src/main/resources/templates/  # Telas: index, novo, editar, login, register
```

## Rotas

### Interface web

| Método | Rota | Descrição |
|---|---|---|
| GET | `/login` | Tela de login |
| GET | `/cars` | Lista de carros |
| POST | `/cars/add` | Cadastra um carro |
| GET | `/cars/delete/{id}` | Remove um carro |

### API REST

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Cria um usuário |
| POST | `/api/auth/login` | Autentica e retorna o token JWT |
| GET | `/api/cars` | Lista carros |
| GET | `/api/cars/{id}` | Busca um carro |
| POST | `/api/cars` | Cria um carro |
| PUT | `/api/cars/{id}` | Atualiza um carro |
| DELETE | `/api/cars/{id}` | Remove um carro |

Envie o token no header `Authorization: Bearer <token>` nas rotas protegidas.

## Como rodar

Pré-requisito: **JDK 21**.

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

| Recurso | URL |
|---|---|
| Aplicação web | `http://localhost:8080/cars` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| Console H2 | `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:carrosdb`, usuário `sa`) |

Para rodar os testes:

```bash
./mvnw test
```

## Problemas conhecidos

Hoje o `SecurityConfig` exige autenticação nas rotas `/api/auth/**`, no Swagger UI e no console H2, e não existe `GET /register`. A correção está em [#2](https://github.com/jullysilva/TheCar/issues/2).
