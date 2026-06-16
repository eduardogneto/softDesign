# Aquisição de Crédito — Desafio Técnico Sicredi

Desenvolvido por **Eduardo Gavronski Neto**

API REST para contratação e consulta de operações de crédito, desenvolvida em Java 17 com Spring Boot 4.1.0.

---

## Tecnologias

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway
- Lombok
- MapStruct
- JUnit 5 + Mockito

---

## Pré-requisitos

- Java 17+
- PostgreSQL rodando localmente (ou via Docker)
- Maven (ou use o `./mvnw` incluso no projeto)

---

## Configuração do banco de dados

Suba via Docker Compose:

```bash
docker compose up -d
```

---

## Como executar

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

---

## Como testar

```bash
./mvnw test
```

---

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints

### POST /operacoes
Contrata uma operação de crédito.

**Request:**
```json
{
  "idAssociado": 1,
  "valorOperacao": 2000,
  "segmento": "PF",
  "codigoProdutoCredito": "101A",
  "codigoConta": "1234567890",
  "areaBeneficiadaHa": null
}
```

**Response (201 Created):**
```json
{
  "idOperacaoCredito": 1
}
```

---

### GET /operacoes/{idOperacaoCredito}
Consulta uma operação de crédito pelo identificador.

**Response (200 OK):**
```json
{
  "idOperacaoCredito": 1,
  "idAssociado": 1,
  "valorOperacao": 2000.00,
  "segmento": "PF",
  "codigoProdutoCredito": "101A",
  "codigoConta": "1234567890",
  "areaBeneficiadaHa": null,
  "dataHoraContratacao": "2026-06-15T19:00:00"
}
```

---

## Regras de negócio

- Operações **AGRO** exigem `areaBeneficiadaHa` preenchido e maior que zero
- Operações **PJ** geram um vínculo adicional na tabela `socio_beneficiario`
- A contratação é validada via serviço externo de produtos de crédito
- Falhas no serviço externo são tratadas com **retry automático** (3 tentativas com backoff)
