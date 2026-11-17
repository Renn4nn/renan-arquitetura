# Aula 8 — Vendas integradas ao hub

O projeto `mvc` parte do MVC da aula 6 e acrescenta o fluxo de pedidos. Cada pedido contém um produto, quantidade, total calculado e e-mail do cliente. Ao fechar, o MVC verifica o estoque, chama `POST /fatura` no hub da aula 7, baixa o saldo e grava o `faturaId` devolvido.

Para executar, inicie `aula-7/hexagonal` na porta 8081 e depois este projeto na porta 8080, ambos com Java 21. Abra `http://localhost:8080/sales`. O endereço do hub fica em `mvc/src/main/resources/application.properties` como `hub.base-url`.

Use `admin` / `admin123` para cadastrar produtos, `estoque` / `estoque123` para movimentar o saldo e `vendedor` / `vendedor123` para criar e fechar pedidos. São usuários locais de demonstração. Cadastre um produto, faça uma entrada em estoque, crie um pedido e clique em **Fechar e faturar**. O ID da fatura aparecerá na tabela de pedidos.

A [análise dos dois problemas de integração](./analise.md) cobre atraso e chamada duplicada. O cliente REST tem timeout, mas o circuit breaker e a idempotência no hub estão documentados como soluções propostas. As saídas fila, SOAP e SMTP do hub continuam simuladas conforme a atividade 7.
