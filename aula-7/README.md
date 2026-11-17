# Aula 7 — Hub de faturamento

Execute `hexagonal/mvnw.cmd spring-boot:run` no Windows ou `./hexagonal/mvnw spring-boot:run` em um ambiente com Java 21. O servidor usa a porta 8081.

`POST http://localhost:8081/fatura` com JSON:

```json
{"pedidoId": 123, "valor": 50.00, "emailCliente": "cliente@exemplo.com"}
```

Resposta: HTTP 201 com `{"faturaId":"..."}`. O hub persiste o ID, pedido, valor, e-mail, data, código de barras de 44 dígitos, chave fiscal de 44 dígitos e hash SHA-256 do conteúdo do e-mail.

Os adaptadores de fila, SOAP e SMTP são **simulações locais** conforme o enunciado. A fila é consumida imediatamente para produzir um código de barras; nenhum broker, serviço fiscal ou servidor de e-mail real é acionado. O envio de e-mail é representado pelo hash de seu conteúdo.
