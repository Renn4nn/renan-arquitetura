# Atividade Aula 3

**Disciplina:** Arquitetura de Software BES <br>
**Atividade:** Aula 3 — Análise de Estilos Arquiteturais<br>
**Aluno:** Renan Augusto Vieira Duarte

---

## 1. Monolito

### Conceito e Definição

No estilo arquitetural monolítico, todas as regras de negócio, interfaces de usuário (ou camadas de apresentação/APIs) e o acesso a dados estão integrados em uma **única unidade de implantação e base de código**.

Na prática, toda a aplicação roda dentro do mesmo processo de sistema operacional. Os módulos internos comunicam-se diretamente via chamadas de funções e métodos em memória, compartilhando o mesmo ciclo de vida: desenvolvimento, compilação, teste e publicação ocorrem como um bloco único (_all-in-one_).

---

### Casos de Uso Comuns

Este estilo é altamente recomendado para:

- **Início de produtos (MVPs e startups):** quando a velocidade de iteração, validação de hipóteses e simplicidade de infraestrutura são prioridades.
- **Sistemas de complexidade e domínios coesos com equipes pequenas:** onde o custo operacional de orquestrar múltiplos serviços não se justifica.

#### Exemplos Reais e Práticos:

1. **Shopify (Monolito Modular):** Plataforma de e-commerce global que processa bilhões de dólares operando sobre um gigantesco monolito em _Ruby on Rails_. Para viabilizar a escala de desenvolvimento sem os custos de fragmentação, a empresa utiliza um modelo modular com fronteiras de domínio estritas.
2. **Sistemas de Gestão Empresarial (ERPs Desktop/Locais):** Aplicações tradicionais (como PDVs e sistemas de controle de estoque/caixa para pequenas e médias empresas) que integram financeiro, inventário e frente de caixa em um único executável conectado diretamente a uma base de dados relacional (ex.: MySQL/PostgreSQL).

---

### Principais Vantagens

- **Simplicidade de Desenvolvimento e Testes:** Fácil de configurar localmente, executar testes integrados de ponta a ponta e debugar sem preocupações com latência de rede entre componentes.
- **Facilidade de Implantação (_Deploy_):** O processo de publicação é direto (geração de um único binário, pacote JAR/WAR ou contêiner Docker).
- **Desempenho de Comunicação Interna:** Todas as chamadas entre módulos acontecem em memória, eliminando custos de serialização de dados (JSON/Protobuf) e latência de rede.
- **Transacionalidade Simples (ACID):** Gerenciamento nativo de transações e integridade de dados garantidos diretamente pelo banco de dados centralizado.

---

### Principais Desvantagens e Gargalos

- **Acoplamento Progressivo:** Com o crescimento da equipe e da base de código, a tendência natural é o surgimento de acoplamento indevido (_big ball of mud_), dificultando manutenções.
- **Dificuldade de Escalabilidade Granular:** Não é possível escalar apenas o componente sob alta demanda (por exemplo, o módulo de pagamentos); toda a aplicação precisa ser replicada em novas instâncias, exigindo mais recursos computacionais.
- **Risco Alto no Ciclo de Publicação:** Um bug crítico introduzido em um módulo secundário pode derrubar toda a aplicação em produção.
- **Bloqueio Tecnológico (_Lock-in_):** Obriga todo o time a utilizar a mesma stack de tecnologia (linguagem, runtime e frameworks) para todo o ecossistema.

---

## 2. Publicador / Assinante (Pub/Sub)

### Conceito e Definição

O estilo **Publicador/Assinante (_Publish/Subscribe_)** é um padrão arquitetural baseado em eventos onde os componentes produtores de mensagens (**publicadores**) não enviam dados diretamente a destinatários específicos, mas sim a canais intermediários ou tópicos gerenciados por um intermediário (_Broker_ / _Event Bus_).

Os consumidores de dados (**assinantes**) registram interesse em tópicos específicos e recebem as mensagens de forma reativa e assíncrona assim que são publicadas. O publicador opera sem conhecimento de quais ou quantos assinantes processarão a informação.

---

### Casos de Uso Comuns

Este estilo é recomendado para:

- **Desacoplamento de fluxos assíncronos e processamento em segundo plano:** cenários onde ações secundárias (notificações, envio de e-mails, auditorias) não devem bloquear a resposta principal dada ao usuário.
- **Arquiteturas Orientadas a Eventos (EDA) e Integração de Microsserviços:** sistemas distribuídos com múltiplos serviços que precisam reagir a mudanças de estado de maneira independente.

#### Exemplos Reais e Práticos:

1. **Processamento de Checkout e Notificações em E-commerces:** Ao finalizar uma compra, o _Serviço de Pedidos_ publica um evento `OrderPlaced`. De forma desacoplada, múltiplos serviços reagem simultaneamente: o _Serviço de E-mail_ dispara a confirmação, o _Serviço de Estoque_ reserva os itens e o _Serviço de Analytics/BI_ contabiliza as métricas de conversão.
2. **Filas de Tarefas e Envio de E-mails com RabbitMQ (ex.: Plataforma Cientista Sem Jaleco):** Quando um usuário solicita inscrição em uma mentoria, a API apenas emite um evento para uma fila no RabbitMQ. Um _worker_ de disparo de e-mails consome a mensagem e executa o envio em segundo plano, liberando a interface do usuário instantaneamente.

---

### Principais Vantagens

- **Desacoplamento Temporal e Estrutural Extremo:** Publicadores e assinantes não precisam se conhecer nem estar disponíveis ao mesmo tempo; o _broker_ gerencia a entrega e retenção temporária das mensagens.
- **Alta Escalabilidade e Extensibilidade:** Novos assinantes podem ser adicionados para escutar eventos existentes sem alterar uma única linha de código do publicador.
- **Resiliência e Amortecimento de Carga (_Backpressure_):** Picos de tráfego são retidos na fila/tópico, permitindo que os consumidores processem no seu próprio ritmo sem sobrecarregar a infraestrutura.

---

### Principais Desvantagens e Gargalos

- **Complexidade de Rastreabilidade e _Debugging_:** Seguir o fluxo de uma transação distribuída exige ferramentas dedicadas de observabilidade (_Distributed Tracing_, ex.: OpenTelemetry/Jaeger) e identificadores de correlação (_Correlation IDs_).
- **Consistência Eventual:** A sincronização entre sistemas não é imediata; regras de negócio precisam tolerar estados temporariamente inconsistentes (_Eventual Consistency_).
- **Ponto Único de Falha / Sobrecarga do Broker:** A infraestrutura de mensageria (ex.: RabbitMQ, Apache Kafka) torna-se o componente crítico do sistema, exigindo configuração robusta de alta disponibilidade, particionamento e políticas de retenção/Dead Letter Queues (DLQ).
