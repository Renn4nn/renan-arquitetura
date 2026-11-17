# Atividade 8 — Problemas na integração entre vendas e faturamento

O fechamento do pedido faz uma chamada REST síncrona ao hub. O MVC envia `pedidoId`, `valor` e `emailCliente` e só marca o pedido como `FECHADO` depois de receber o `faturaId`. Os dois cenários abaixo são riscos da comunicação pela rede estudados na aula 8.

## 1. Hub lento ou indisponível

Se o hub demorar ou não responder, o vendedor aguarda o fechamento e pode ver uma mensagem de erro. O pedido permanece `ABERTO`, sem `faturaId`, e o estoque local não é baixado. Para o negócio, a venda ainda não está confirmada e precisa de nova tentativa ou atendimento.

**Solução:** um timeout limita a espera; um circuit breaker, se várias chamadas falharem, interrompe novas tentativas temporariamente e responde rápido. O timeout de conexão e leitura já está configurado no cliente REST do MVC. O circuit breaker atuaria no mesmo cliente, antes da chamada ao hub. Ele é uma proposta de melhoria, não uma implementação desta entrega.

## 2. Resposta perdida e chamada repetida

O hub pode criar a fatura e a resposta se perder na rede. O MVC vê uma falha e mantém o pedido `ABERTO`, embora já exista uma fatura no hub. Ao tentar fechar outra vez, pode ser criada uma segunda fatura, com outro boleto e outra notificação. O vendedor vê uma tentativa que aparentemente falhou; o cliente pode receber cobrança duplicada.

**Solução:** idempotência no hub por `pedidoId`. Antes de emitir uma fatura, o hub procuraria uma já criada para aquele pedido e devolveria o mesmo `faturaId`. A restrição deve estar no banco do hub para cobrir também chamadas simultâneas. Ela atuaria no endpoint e no serviço de faturamento, antes dos adaptadores de boleto, nota e e-mail. O MVC já evita reenviar pedidos marcados como `FECHADO`, mas isso não resolve a resposta perdida; a idempotência no hub ainda é necessária.

Também há uma fronteira de transação: salvar o pedido no MVC e criar a fatura no hub não formam uma única transação de banco. Por isso a aplicação não deve tratar uma falha de rede como prova de que o hub não criou a fatura.
