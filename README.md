# Baozi Store

Atividade prática de Desenvolvimento Web Back End.

API REST Java 17 com Spring Boot 4.1.2, Spring Data JPA e H2 persistente em arquivo. Não requer instalar MySQL ou Maven: o Maven Wrapper está incluído.

## Executar no Windows

Abra esta pasta no VS Code e execute no terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

A API atende em `http://localhost:8080`. A raiz `/` não possui página: use as rotas abaixo. Pare com Ctrl+C. O banco fica em `data/`, que não é enviado ao GitHub.

## Rotas

Para cada recurso `/clientes`, `/produtos` e `/pedidos`:

| Método | Caminho | Resposta |
|---|---|---|
| POST | /recurso | 201 e registro criado |
| GET | /recurso | 200 e lista |
| GET | /recurso/{id} | 200 ou 404 |
| DELETE | /recurso/{id} | 204, 404 ou 409 |

Cliente: `id`, `nome`, `clienteDesde` (AAAA-MM-DD).
Produto: `id`, `nome`, `preco` (BigDecimal), `estoque` (Boolean de disponibilidade).
Pedido: `id`, `clienteId`, `produtoId`, `quantidade` (inteiro positivo).

O banco usa chaves estrangeiras: um pedido referencia exatamente um cliente e um produto. Exclua os pedidos antes de excluir o cliente ou produto vinculado. Produto indisponível não pode ser comprado. Dados inválidos retornam 400; registros ausentes, 404; conflitos, 409. Não há autenticação, conforme o escopo da atividade. Não há controle numérico de estoque nem campo de total do pedido.

## Postman

Importe `postman/BaoziStore.postman_collection.json`. Execute na ordem 01 a 12. Os IDs retornados são salvos automaticamente em variáveis; não é preciso assumir que o ID será 1. Os testes verificam os status HTTP.

Os exemplos usam o cliente Cliente Exemplo, Baozi de porco a R$ 8,50 e pedido de três unidades. Tire capturas reais antes de executar as exclusões. A coleção não substitui os prints obrigatórios do trabalho.

## Organização

- `model`: entidades JPA e relacionamentos.
- `repository`: persistência com JpaRepository.
- `controller`: endpoints e tratamento de erros.
- `postman`: coleção importável com os 12 endpoints.

## Testes

```powershell
.\mvnw.cmd test
```

O teste automatizado usa banco H2 em memória separado dos dados locais.

