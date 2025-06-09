
# 📦 Manual de Instalação — Back-End API (Java Spring Boot)

## ✅ Pré-requisitos

Antes de iniciar, certifique-se de que você tem instalado:

- [Java 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) *(opcional, mas recomendado)*
- Banco de Dados MySQL (ou configure no `application.properties`)

## 🚀 Como rodar o projeto

### 1. Clone este repositório em sua máquina

```bash
git clone <URL-do-repositório>
```

Ou descompacte o `.zip` que você recebeu.

### 2. Configure o banco de dados

No arquivo:

```
src/main/resources/application.properties
```

Adicione suas credenciais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

Se você tiver um script `.sql`, crie as tabelas no seu MySQL.

### 3. Compile e execute

Entre na raiz do projeto (onde está o `pom.xml`) e rode:

```bash
./mvnw spring-boot:run
```

ou, se preferir compilar:

```bash
./mvnw clean install
java -jar target/*.jar
```

### 4. Acesse o sistema

Após iniciado, acesse via navegador ou Postman:

```
http://localhost:8080
```

Endpoints disponíveis incluem:

- `/agendamento`
- `/cliente`
- `/medico`
- `/servicos`

## 🐳 Rodando com Docker (opcional)

Você pode subir o projeto com:

```bash
docker-compose up --build
```

Verifique o `docker-compose.yml` para configurar o banco de dados, se necessário.

## ➕ Adicionando novos recursos (entidades / APIs)

Para adicionar um novo recurso:

1. Crie a entidade no pacote `model`.
2. Crie o repositório (`JpaRepository`).
3. Crie o `Service` com a lógica de negócio.
4. Crie o `Controller` com os endpoints (`@RestController`).

## 🧠 Dica sobre CRUD e HTTP

| CRUD   | Ação      | Verbo HTTP | Anotação Spring |
|--------|-----------|------------|------------------|
| Create | Criar     | POST       | `@PostMapping`   |
| Read   | Ler       | GET        | `@GetMapping`    |
| Update | Atualizar | PUT        | `@PutMapping`    |
| Delete | Deletar   | DELETE     | `@DeleteMapping` |
