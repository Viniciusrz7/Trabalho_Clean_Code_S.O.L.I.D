# Projeto de Disciplina: Sistema de Gerenciamento de Pedidos

Este documento descreve a implementação técnica do Sistema de Gerenciamento de Pedidos para Lanchonete, desenvolvido como requisito avaliativo da disciplina. O projeto tem como objetivo demonstrar a aplicação prática de Clean Code, princípios S.O.L.I.D. e Padrões de Projeto (GoF) em um contexto de negócio real.

## 1. Contextualização e Escopo do Projeto

O sistema opera no domínio de vendas alimentícias. A arquitetura foi estruturada em camadas lógicas (Domínio, Serviço, Integração e Pagamento) visando o encapsulamento das regras de negócio.

- **Fronteiras Internas (Domínio):** Modelagem estrutural do Pedido, Itens do Pedido, Produtos e lógicas de cálculo de totais.
- **Fronteiras Externas e Integrações (Diferencial):** Conforme sugerido nas orientações da disciplina, o projeto apresenta integração com serviços simulados de terceiros, incluindo:
  - Gateway de Pagamento Externo (processamento de transações via Cartão e Pix).
  - Serviço de Inteligência Artificial (Integração com Gemini para sugestões baseadas no hábito de consumo).
  - API de Notificação Push (mensageria para atualização de status do pedido).

Os principais atores que interagem com o sistema são os usuários finais operando a interface via console, e as interfaces de integração (APIs) que respondem aos eventos do sistema.

## 2. Aplicação de Práticas de Clean Code

O código-fonte foi desenvolvido seguindo as diretrizes de código limpo para garantir legibilidade e manutenibilidade:

- **Nomenclatura Significativa:** Variáveis, métodos e classes possuem nomes claros e descritivos em relação ao domínio da aplicação (exemplo: `prepararValorDoPagamento`, `GatewayPagamento`).
- **Funções Coesas:** Os métodos foram extraídos para realizar apenas uma operação por vez, possuindo tamanho reduzido e limitando-se ao máximo de dois parâmetros em suas assinaturas (exemplo: `processarEscolhaProduto`).
- **Prevenção de Aninhamentos (Guard Clauses):** Substituição de estruturas condicionais (if/else) complexas por retornos antecipados para tratamento de erros, reduzindo a complexidade ciclomática.
- **Recursos Modernos e Linguagem Funcional:** Substituição de validações de nulos e laços de repetição tradicionais por construções baseadas na API de Streams, como `Optional`, `anyMatch` e laços `forEach`. Essa abordagem foca na intenção da lógica em vez de sua mecânica iterativa.
- **Tratamento de Recursos:** Utilização da estrutura `try-with-resources` na classe `Main` para garantir o fechamento seguro de instâncias de entrada (Scanner), eliminando a necessidade de blocos `finally` explícitos.

## 3. Implementação dos Princípios S.O.L.I.D.

O projeto implementa de forma estrutural múltiplos pilares da orientação a objetos, com destaque para:

### Princípio da Responsabilidade Única (SRP)
- **Localização:** Classe `PedidoService` e classe `ItemPedido`.
- **Explicação:** A classe `PedidoService` detém unicamente a responsabilidade de orquestrar o fluxo de processamento de um pedido (processar pagamento e notificar instâncias). Cálculos matemáticos foram mantidos dentro das classes de domínio (como `Pedido.calcularTotal()`), garantindo que os objetos gerenciem seu próprio estado (Modelo de Domínio Rico). A classe `ItemPedido` é responsável exclusiva por validar a integridade lógica de sua quantidade durante a instanciação.

### Princípio Aberto/Fechado (OCP)
- **Localização:** Interface `DescontoStrategy` e `GatewayPagamento`.
- **Explicação:** O sistema permite a introdução de novos algoritmos de desconto ou novos provedores de pagamento mediante a criação de novas classes implementando essas interfaces, abolindo a necessidade de modificar as classes orquestradoras existentes.

### Princípio da Inversão de Dependência (DIP)
- **Localização:** Construtor da classe `PedidoService`.
- **Explicação:** O serviço principal depende exclusivamente de abstrações (`GatewayPagamento`, `ServicoSugestaoIA`) e não de implementações concretas de infraestrutura. A composição dessas dependências é realizada na entrada do projeto (Composition Root / Classe `Main`), mitigando o acoplamento estrutural.

## 4. Utilização de Padrões de Projeto (GoF)

Foram implementados padrões arquiteturais correspondentes às três classificações estipuladas no escopo: Criacional, Estrutural e Comportamental.

### Padrão Criacional: Simple Factory
- **Localização:** Classe `PagamentoFactory`.
- **Objetivo e Explicação:** Encapsular a lógica condicional de criação dos objetos concretos de pagamento (`PagamentoPix`, `PagamentoCartao`). O módulo cliente requisita um tipo primitivo e a fábrica centraliza e abstrai a complexidade de instanciar o objeto adequado de domínio.

### Padrão Estrutural: Adapter
- **Localização:** Classes `GatewayPagamentoAdapter` e `AdaptadorSugestaoGemini`.
- **Objetivo e Explicação:** Promover a compatibilidade de interface esperada pelo domínio interno da aplicação perante bibliotecas e serviços de terceiros (como o serviço simulado `GatewayExterno`). O adaptador processa transformações de dados mandatórias, como a injeção e geração de um UUID de transação local, garantindo a interoperabilidade de sistemas sem corromper as abstrações internas de domínio.

### Padrão Comportamental 1: Strategy
- **Localização:** Interface `DescontoStrategy` e classe concreta `DescontoPromocional`.
- **Objetivo e Explicação:** Isolar e encapsular algoritmos de regras de negócio suscetíveis a mudanças sazonais (cálculo de descontos) em classes autônomas e intercambiáveis durante a execução do programa, orientando as regras de precificação a contrato.

### Padrão Comportamental 2: Observer
- **Localização:** Interface `ObservadorNotificacaoPush` e `AdaptadorNotificacaoPush`.
- **Objetivo e Explicação:** Consolidar a arquitetura reativa através de relacionamento de um-para-muitos. Após a confirmação síncrona do processamento no `PedidoService`, os eventos de troca de estado (status de pedido) são disparados às instâncias assinantes registradas, promovendo o desacoplamento de responsabilidades entre módulos distintos do sistema.
