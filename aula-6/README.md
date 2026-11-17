# Aula 6 — Módulo de estoque

O projeto `mvc` implementa cadastro de produto, entrada e saída de estoque, status do produto e descontinuação restrita ao ADM. Os quatro diagramas em `diagramas/` descrevem as duas sequências, o fluxo de saída e os estados do produto.

Execute `mvc/mvnw.cmd spring-boot:run` no Windows ou `./mvc/mvnw spring-boot:run` em um ambiente com Java 21. Abra `http://localhost:8080/`.

Usuários de demonstração: `admin` / `admin123` cadastra e descontinua produtos; `estoque` / `estoque123` movimenta o estoque. As senhas são apenas para execução local da atividade.

Um produto novo começa `INDISPONIVEL`, com saldo zero. A primeira entrada o torna `DISPONIVEL`; a saída que zera o saldo o torna `INDISPONIVEL`. Produto `DESCONTINUADO` não aceita movimentações.
