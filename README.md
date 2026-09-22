# ProceduresAndTriggers

Mini banco com Java 21, Spring Boot, Spring Data JPA e PostgreSQL para demonstrar Stored Procedures, Triggers e Flyway.

## Regra do projeto

CRUD simples continua no Java/JPA.

As operações que possuem lógica financeira são executadas no PostgreSQL:

- depósito;
- débito;
- transferência.

O Spring Data JPA chama essas operações através de `@Procedure`.

## Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Lombok
- Docker Compose

## Arquitetura

Controller -> Service -> Repository JPA -> PostgreSQL Procedure -> Trigger

O CRUD de usuários e contas usa `JpaRepository`.

As operações financeiras usam:

`@Procedure` -> `sp_depositar`
`@Procedure` -> `sp_debitar`
`@Procedure` -> `sp_transferir`

Não há `JdbcTemplate` no projeto.

## Procedures

### sp_depositar

Valida o valor, verifica se a conta está ativa, altera o saldo e registra a transação.

### sp_debitar

Valida o valor, bloqueia a conta com `FOR UPDATE`, verifica o saldo disponível, debita e registra a transação.

### sp_transferir

Valida origem e destino, bloqueia as duas contas, verifica o saldo da origem, debita uma conta, credita a outra e registra a transferência.

Todas essas regras ficam no PostgreSQL.

## Trigger

A trigger `trg_conta_auditoria_saldo` é executada quando o saldo da conta é alterado.

Ela registra:

- conta;
- saldo anterior;
- saldo novo;
- data da alteração.

Também atualiza automaticamente `atualizado_em`.

## Flyway

As migrations ficam em `src/main/resources/db/migration`:

- `V1__create_tables.sql`
- `V2__create_procedures.sql`
- `V3__create_triggers.sql`

O Flyway executa as migrations automaticamente na inicialização.

## Endpoints

### Usuários

`POST /usuarios`

`GET /usuarios`

`GET /usuarios/{id}`

`POST /usuarios/{usuarioId}/conta`

### Operações

`POST /operacoes/depositar`

`POST /operacoes/debitar`

`POST /operacoes/transferir`

`GET /operacoes/contas/{contaId}`

### Exemplo de depósito

~~~json
{
  "contaId": 1,
  "valor": 500.00
}
~~~

### Exemplo de transferência

~~~json
{
  "contaOrigemId": 1,
  "contaDestinoId": 2,
  "valor": 150.00
}
~~~

## Executar

Suba o PostgreSQL:

~~~bash
docker compose up -d
~~~

Execute a aplicação:

~~~bash
./mvnw spring-boot:run
~~~

No Windows:

~~~powershell
./mvnw.cmd spring-boot:run
~~~

O Hibernate valida o schema e o Flyway cria as tabelas, procedures e triggers.

## Transação

`@Transactional` fica no Service.

Assim, a chamada da procedure participa da transação da aplicação. Se uma procedure lançar uma exceção, a operação é revertida.

## Objetivo de estudo

Este projeto demonstra uma situação em que colocar uma regra de negócio no banco pode ser intencional: operações financeiras que precisam executar várias alterações de forma atômica.

A ideia não é usar Stored Procedures para todo CRUD, mas mostrar como integrar Spring Data JPA com lógica transacional existente no PostgreSQL.
