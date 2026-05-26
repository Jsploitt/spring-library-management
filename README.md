# Library Management System

A REST API for managing a library book archive. Built with Spring Boot, JPA, MySQL, and Liquibase as part of a 9-week backend engineering training programme.

## Tech stack

- Java 17
- Spring Boot 3 (Web, Data JPA, Security)
- Spring Security with JWT authentication
- MySQL 8
- Liquibase
- Maven
- JJWT 0.12.6

---

## Local setup

**Prerequisites:** Java 17+, Maven, MySQL 8 running locally.

**1. Clone the repo**
```bash
git clone https://github.com/<your-username>/library-management.git
cd library-management
```

**2. Create the database**
```sql
CREATE DATABASE library_db;
```

**3. Configure your environment**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
Then edit `application.properties` and fill in your DB credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**4. Run**
```bash
./mvnw spring-boot:run
```

Liquibase will apply all migrations automatically on startup. The API is available at `http://localhost:8080`.

---

## Authentication & Security

The API uses **JWT (JSON Web Token)** authentication with role-based access control (RBAC).

### User Roles

- **USER** - Can view books (read-only access)
- **LIBRARIAN** - Can view, add, update, and delete books
- **ADMIN** - Full access to all endpoints including admin operations

### Getting Started

1. **Register a new user** via `POST /api/v1/auth/register`
2. **Login** via `POST /api/v1/auth/login` to receive a JWT token
3. **Include the token** in subsequent requests using the `Authorization: Bearer <token>` header

### Security Features

- JWT-based stateless authentication
- BCrypt password encryption
- Role-based authorization on endpoints
- Method-level security annotations
- Custom authentication entry point for unauthorized access

---

## API reference

### Authentication Endpoints

Base path: `/api/v1/auth` (Public - no authentication required)

| Method | Endpoint                  | Description          | Status codes        |
|--------|---------------------------|----------------------|---------------------|
| `POST` | `/api/v1/auth/register`   | Register a new user  | `201`, `400`, `409` |
| `POST` | `/api/v1/auth/login`      | Login and get token  | `200`, `401`        |

#### POST /api/v1/auth/register

**Request body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Validation rules:**
- `username`: 3-50 characters, required
- `email`: Valid email format, required
- `password`: Minimum 8 characters, required

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

#### POST /api/v1/auth/login

**Request body:**
```json
{
  "username": "johndoe",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

---

### Book Management Endpoints

Base path: `/api/v1/books` (Authentication required)

| Method   | Endpoint                              | Description                   | Required Role       | Status codes        |
|----------|---------------------------------------|-------------------------------|---------------------|---------------------|
| `POST`   | `/api/v1/books`                       | Add a new book                | ADMIN, LIBRARIAN    | `201`, `400`, `409` |
| `GET`    | `/api/v1/books/{isbn}`                | Get a book by ISBN            | USER, LIBRARIAN, ADMIN | `200`, `404`        |
| `GET`    | `/api/v1/books?genre={genre}`         | Get all books by genre        | USER, LIBRARIAN, ADMIN | `200`               |
| `GET`    | `/api/v1/books/count?author={author}` | Count books by author         | USER, LIBRARIAN, ADMIN | `200`               |
| `DELETE` | `/api/v1/books/{isbn}`                | Delete a book by ISBN         | ADMIN, LIBRARIAN    | `204`, `404`        |

**Authentication header required:**
```
Authorization: Bearer <your-jwt-token>
```

### POST /api/v1/books — request body

The `bookType` field determines which subtype is created. Each type requires its own additional fields.

**AudioBook**
```json
{
  "bookType": "AUDIO",
  "isbn": 1230,
  "title": "Clean Code",
  "author": "Robert Martin",
  "genre": "Technology",
  "duration": 480,
  "narrator": "John Smith"
}
```

**PrintedBook**
```json
{
  "bookType": "PRINTED",
  "isbn": 1230,
  "title": "Clean Code",
  "author": "Robert Martin",
  "genre": "Technology",
  "numOfPages": 431,
  "hardcover": true
}
```

**EBook**
```json
{
  "bookType": "EBOOK",
  "isbn": 1230,
  "title": "Clean Code",
  "author": "Robert Martin",
  "genre": "Technology",
  "numOfPages": 431,
  "sizeMb": 12
}
```

### ISBN validation

ISBNs are 4-digit integers. The fourth digit is a control digit verified using:

```
(n1 × 3 + n2 × 2 + n3 × 1) mod 4 = n4
```

Example: `0200` is valid. `1234` is not.

### Error responses

| Status | Meaning                                                  |
|--------|----------------------------------------------------------|
| `400`  | Missing or invalid `bookType`, or failed ISBN validation |
| `401`  | Unauthorized - missing, invalid, or expired JWT token    |
| `403`  | Forbidden - insufficient permissions for this operation  |
| `404`  | No book found for the given ISBN                         |
| `409`  | A book with this ISBN already exists, or username/email already taken |

---

## Database & Liquibase

Schema is managed entirely by Liquibase and applied at startup — no manual DDL required.

### Changelog structure

```
src/main/resources/db/changelog/
├── db.changelog-master.yaml              # Orchestrator — includes all changesets in order
└── changes/
    ├── 001-create-book-tables.xml        # Creates book + all subtype tables (source of truth)
    ├── 002-add-book-type-column.xml      # Superseded by 001; guarded with precondition
    ├── 003-create-book-subtype-tables.xml # Superseded by 001; guarded with preconditions
    └── 004-create-security-tables.xml    # Creates users, roles, and user_roles tables
```

Changesets `002` and `003` are retained for history but will be safely skipped on any database state via `preConditions onFail="MARK_RAN"`.

### Schema design

**Book tables** use JPA **JOINED table inheritance** with `book_type` as the discriminator column. Each book type has its own table keyed by the same `id` as the `book` base table.

```
book              (id, book_type, isbn, author, title, genre, ref_code)
├── audio_book    (id → book.id, duration, narrator)
├── printed_book  (id → book.id, num_of_pages, hardcover)
└── e_book        (id → book.id, num_of_pages, size_mb)
```

**Security tables** manage users, roles, and authentication:

```
users         (id, username, email, password, enabled, account_non_locked, created_at, updated_at)
roles         (id, name)
user_roles    (user_id → users.id, role_id → roles.id)  [many-to-many join table]
```

Default roles seeded on initialization: `ROLE_USER`, `ROLE_LIBRARIAN`, `ROLE_ADMIN`

---

## Project structure

```
src/main/java/com/moutaz/library/
├── auth/
│   ├── controller/   # AuthController — registration & login
│   ├── service/      # AuthService interface + AuthServiceImpl
│   └── dto/          # RegisterRequest, LoginRequest, AuthResponse
├── book/
│   ├── controller/   # BookController — HTTP routing
│   ├── service/      # BookService interface + BookServiceImpl
│   ├── repository/   # BookRepository — JPA derived queries
│   ├── domain/       # Book, AudioBook, PrintedBook, EBook entities
│   └── dto/          # BookRequest
├── security/
│   ├── config/       # SecurityConfig — security filter chain, auth beans
│   └── jwt/          # JWT token provider, authentication filter, entry point
└── user/
    ├── entities/     # User, Role entities
    ├── repository/   # UserRepository, RoleRepository
    └── service/      # UserService interface + UserServiceImpl
```

---

## Roadmap

| Week | Focus |
|------|-------|
| ✅ 1 | Spring Boot foundations, REST API, JPA, Liquibase |
| ✅ 2 | Spring Security — auth, roles, endpoint protection |
| 3    | Apache Kafka, microservices split, Docker |
| 4    | Unit & integration testing (JUnit 5, Mockito) |
| 5    | Dockerfile optimisation, container registry |
| 6    | Kubernetes on GKE |
| 7    | Helm charts, ArgoCD GitOps |
| 8    | Prometheus, Grafana, Loki observability |
| 9    | GitHub Actions CI/CD, capstone demo |
