# irrah-tech-backend

Backend Java 21/Spring Boot para o exercício técnico de processamento de mensagens com fila em memória.

## Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- JPA/Hibernate

## Execução

Configure o banco via variáveis de ambiente, se necessário:

```bash
DB_URL=jdbc:postgresql://localhost:5432/irrah
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

Depois execute:

```bash
mvn spring-boot:run
```

Por padrão, a aplicação usa `ddl-auto=update`.

## Autenticação simples

O endpoint `POST /auth` autentica pelo `documentId` e retorna o `clientId`.

Para endpoints de conversas e mensagens, envie o cliente autenticado no header:

```http
X-Client-Id: client-id
```

## Endpoints

### Autenticação e clientes

- `POST /auth`
- `GET /clients`
- `POST /clients`
- `GET /clients/{id}`
- `PUT /clients/{id}`
- `GET /clients/{id}/balance`

### Conversas

- `GET /conversations`
- `GET /conversations/{id}`
- `GET /conversations/{id}/messages`

### Mensagens

- `POST /messages`
- `GET /messages?status=queued&priority=urgent`
- `GET /messages/{id}`
- `GET /messages/{id}/status`

### Fila

- `GET /queue/status`

## Regras implementadas

- Clientes pré-pagos debitam saldo a cada mensagem.
- Clientes pós-pagos consomem limite disponível a cada mensagem.
- Mensagens normais custam `0.25`; urgentes custam `0.50`.
- Mensagens entram como `queued` e são processadas por worker agendado.
- A fila usa duas filas em memória: normal e urgente.
- A priorização evita starvation: após três mensagens urgentes seguidas, uma normal é processada se existir.
- Consultas de cliente e saldo usam cache simples em memória.
