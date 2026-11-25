# Energia Renovável

Sistema acadêmico de gerenciamento e monitoramento de energia renovável, desenvolvido para a disciplina de Programação e Otimização de Banco de Dados do segundo período do curso de Análise e Desenvolvimento de Sistemas na UNIBRA.
Alunos: Soloncy Moura e Douglas Braga.

 O projeto simula a geração de energia solar e eólica, o consumo de uma unidade consumidora e o respectivo balanço energético, armazenando medições associadas a timestamps em um banco PostgreSQL com TimescaleDB.

## Problema

Acompanhar a geração e o consumo de energia de fontes renováveis pode ser difícil quando os dados estão distribuídos ou não são analisados ao longo do tempo. Sem esse acompanhamento, fica mais difícil identificar períodos de déficit, excedente ou baixa eficiência.

## Objetivo

Desenvolver uma aplicação que permita representar unidades geradoras e seus equipamentos, registrar consumidores e acompanhar temporalmente:

- a energia gerada por fontes solares e eólicas;
- a energia consumida por equipamentos associados a consumidores;
- o balanço entre geração e consumo;
- a evolução dessas grandezas ao longo do tempo.

## Funcionalidades e escopo

O escopo acadêmico contempla:

- cadastro de usuários;
- cadastro de unidades geradoras;
- cadastro e associação de equipamentos de geração;
- cadastro de consumidores;
- simulação de equipamentos solares e eólicos;
- simulação de cargas elétricas residenciais;
- registro periódico de medições;
- cálculo do balanço energético.

O projeto foi mantido intencionalmente simples, priorizando a demonstração da integração entre aplicação Java, banco relacional e armazenamento de séries temporais.

## Tecnologias

- **Java** — implementação da aplicação, das entidades e da simulação;
- **JDBC** — comunicação entre Java e banco de dados;
- **PostgreSQL** — persistência dos dados relacionais;
- **TimescaleDB** — extensão do PostgreSQL voltada ao armazenamento e à consulta de séries temporais.

## Visão geral da arquitetura

A aplicação Java representa os elementos do sistema e executa uma simulação periódica. Em cada ciclo, os equipamentos de geração e consumo fornecem valores de potência; a aplicação consolida esses valores, calcula o balanço e registra uma medição com data e hora.

Fluxo conceitual:

```text
Equipamentos solares ─┐
                      ├─> Geração total ─┐
Turbinas eólicas ─────┘                  │
                                         ├─> Balanço energético ─> Medições no banco
Equipamentos de consumo ─> Consumo total┘
```

### Modelo de dados

O modelo relacional trabalhado no projeto inclui as seguintes entidades:

| Entidade | Responsabilidade |
|---|---|
| `usuario` | Representar os usuários do sistema |
| `unidade_geradora` | Representar uma unidade de geração de energia |
| `equipamento` | Representar equipamentos associados à geração |
| `consumidor` | Representar a unidade ou entidade cujo consumo é acompanhado |

As medições são dados temporais e podem ser organizadas em séries próprias, conforme o esquema adotado no banco:

| Série / tabela conceitual | Conteúdo |
|---|---|
| `medicao_energia` | Registros temporais da geração |
| `medicao_consumo` | Registros temporais do consumo |
| `balanco_energetico` | Relação entre geração e consumo no instante ou intervalo |

> **Atenção:** os nomes acima representam a organização discutida durante o desenvolvimento. Confira os nomes efetivamente criados no seu script SQL antes de usar este README como documentação definitiva do esquema físico.

## Simulação

A simulação representa condições variáveis de geração e consumo, em vez de registrar apenas valores fixos.

- A geração solar pode considerar variáveis ambientais, como irradiância e temperatura.
- A geração eólica considera a disponibilidade de vento.
- Os equipamentos consumidores podem alternar entre ligados e desligados e apresentar variação de potência.
- O simulador executa ciclos periódicos — o intervalo de 5 segundos foi adotado como referência durante o desenvolvimento.

O balanço é calculado pela diferença:

```text
balanço = energia gerada - energia consumida
```

Interpretação:

- **Saldo positivo:** geração superior ao consumo (excedente).
- **Saldo negativo:** consumo superior à geração (déficit).
- **Saldo igual a zero:** geração e consumo equivalentes.

### Potência e energia

É importante distinguir as grandezas utilizadas:

- **Potência (W):** taxa instantânea de geração ou consumo.
- **Energia (Wh):** potência acumulada ao longo de um intervalo de tempo.

Para uma potência aproximadamente constante durante um intervalo:

```text
energia (Wh) = potência (W) × tempo (h)
```

Assim, em ciclos de poucos segundos, a energia do intervalo deve ser calculada usando a duração real do ciclo, e não confundida com a potência instantânea.

## Banco de dados

O PostgreSQL armazena os dados cadastrais e relacionais. O TimescaleDB, como extensão do PostgreSQL, é utilizado para trabalhar com medições temporais.

A modelagem de séries temporais permite consultar os registros por período e acompanhar a evolução da geração, do consumo e do balanço energético. Quando as tabelas de medição forem configuradas como *hypertables*, o TimescaleDB poderá aplicar seus recursos específicos de particionamento e consulta temporal.

## Execução

### Pré-requisitos

- JDK compatível com a versão utilizada no projeto;
- PostgreSQL instalado e em execução;
- extensão TimescaleDB instalada no servidor, caso o ambiente utilize seus recursos;
- driver JDBC do PostgreSQL disponível no classpath da aplicação;
- banco de dados e tabelas criados a partir do script SQL do projeto.

### Configuração da conexão

A aplicação utiliza uma URL JDBC no formato:

```java
String url = "jdbc:postgresql://HOST:PORTA/NOME_DO_BANCO";
String usuario = "SEU_USUARIO";
String senha = "SUA_SENHA";
```

Substitua os valores pelos dados do ambiente local. Evite versionar senhas ou credenciais reais no repositório; prefira variáveis de ambiente ou um arquivo local ignorado pelo Git.

### Inicialização

1. Inicie o serviço PostgreSQL.
2. Crie o banco de dados utilizado pela aplicação.
3. Habilite a extensão TimescaleDB, se aplicável ao ambiente:

   ```sql
   CREATE EXTENSION IF NOT EXISTS timescaledb;
   ```

4. Execute o script SQL de criação das tabelas e relacionamentos.
5. Configure a conexão JDBC na aplicação.
6. Compile e execute a classe que contém o método `main`, conforme a estrutura do projeto e a IDE ou ferramenta de build utilizada.

> Os comandos exatos de compilação e execução dependem da estrutura do repositório (por exemplo, Maven, Gradle ou projeto Java sem ferramenta de build). Eles não são fixados aqui porque essa estrutura não foi confirmada.

## Estado atual

Com base no desenvolvimento registrado até o momento:

- conexão da aplicação Java ao PostgreSQL estabelecida;
- cadastros básicos executados e identificadores retornados pelo banco;
- equipamentos de geração solar e eólica inicializados na simulação;
- consumidor cadastrado;
- simulação energética iniciada;
- registro e exibição de medições trabalhados na aplicação.

O projeto já alcançou seu objetivo acadêmico central de demonstrar a conexão com o banco, a execução do simulador e o registro de medições. Recursos adicionais de análise, dashboards, alertas ou integração com equipamentos físicos não fazem parte do escopo confirmado.

## Possíveis evoluções

Como extensões futuras, podem ser considerados:

- consultas de geração, consumo e saldo por intervalo de tempo;
- relatórios de períodos com déficit ou excedente;
- análise de eficiência dos equipamentos;
- visualização gráfica das séries temporais.

Essas possibilidades não devem ser interpretadas como funcionalidades já implementadas.

## Contexto acadêmico

Projeto desenvolvido como atividade acadêmica de Programação e Otimizção de Banco de Dados, com foco na integração entre modelagem relacional e séries temporais aplicada à gestão de energia renovável. 

---

**Observação para manutenção:** antes da entrega final, revise os nomes das tabelas e colunas, os relacionamentos, a configuração do TimescaleDB e o método de execução para que esta documentação corresponda exatamente à versão presente no repositório.
