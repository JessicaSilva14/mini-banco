# Mini Banco

Sistema bancário de console em Java, criado para praticar Programação Orientada a Objetos e boas práticas.

## Funcionalidades
- Abertura de conta corrente (com cheque especial) e conta poupança
- Depósito, saque e transferência entre contas
- Extrato com data, tipo e valor de cada operação
- Rendimento de juros na poupança
- Validações com exceções personalizadas

## Tecnologias
- Java 17
- Maven
- JUnit 5

## Como rodar
**Pelo VS Code:** abra a pasta `mini-banco`, abra `Main.java` e clique em **Run** acima do método `main`.

**Pelo terminal:**
```bash
mvn compile exec:java
```

**Testes:**
```bash
mvn test
```

## Decisões técnicas
- `BigDecimal` no lugar de `double` para valores monetários, evitando erros de arredondamento.
- O saldo não tem setter público: só muda por depósito, saque ou transferência.
- Cada tipo de conta define sua regra de saque (`podeSacar`), usando herança e polimorfismo.
- Se o saque de uma transferência falhar, a conta de destino não é alterada.

## Estrutura
```
modelo/   -> Conta, ContaCorrente, ContaPoupanca, Cliente, Transacao
excecao/  -> exceções personalizadas
servico/  -> Banco (regras de negócio e transferências)
Main.java -> menu de console
```

## Próximos passos
- Persistir dados em banco de dados (JDBC)
- Validar CPF
- Interface gráfica ou API REST
