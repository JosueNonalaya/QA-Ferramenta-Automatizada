# Ferramenta-Auditoria de Qualidade
### Sistema Automatizado de Auditoria da Qualidade de Software

Uma aplicação desenvolvida para automatizar o processo de **auditoria de qualidade de software**, permitindo a execução de checklists de qualidade, cálculo da porcentagem de aderência de um projeto e gerenciamento de não conformidades encontradas durante a auditoria.

O sistema busca centralizar o processo de avaliação da qualidade, reduzindo atividades manuais e facilitando o acompanhamento das não conformidades até sua resolução.

Processos de auditoria de qualidade podem envolver o preenchimento manual de checklists, cálculo de indicadores, comunicação de problemas e acompanhamento de pendências.


Propomos centralizar essas atividades em uma única aplicação.

---
## Especificações:
O projeto tem como objetivo implementar uma maneira automatizada de realizar auditorias de qualidade de software, contemplando:

- Criação e execução de um checklist de qualidade;
- Registro dos resultados da auditoria;
- Cálculo automático do percentual de aderência;
- Identificação e registro de não conformidades;
- Associação de não conformidades aos responsáveis;
- Comunicação das não conformidades aos responsáveis;
- Registro do histórico das ocorrências;
- Visualização de métricas relacionadas à qualidade e aderência.

---
## Tecnologias

- Java
- JavaFX
- Git
- GitHub
- IntelliJ IDEA
- MySQL Workbench

> Outras tecnologias e ferramentas poderão ser adicionadas conforme a evolução do projeto.

---
## Arquitetura

Utilizamos uma **arquitetura em camadas (Layered Architecture)**, com o objetivo de separar as responsabilidades do sistema e facilitar sua manutenção, evolução e compreensão pela equipe.

A estrutura planejada segue o fluxo:

```text
Controller
    ↓
 Service
    ↓
   DAO
    ↓
 Database
```


### Estrutura de pastas e responsabilidades

| Pacote        | Responsabilidade                                                                                |
| ------------- | ----------------------------------------------------------------------------------------------- |
| `model/`      | Contém as entidades que representam os objetos e conceitos do domínio do AutoAudit.             |
| `enums/`      | Contém estados, tipos e opções predefinidas utilizados pelo sistema.                            |
| `controller/` | Responsável por receber e encaminhar as ações da camada de apresentação.                        |
| `service/`    | Contém as regras de negócio e coordena as operações do sistema.                                 |
| `dao/`        | Responsável pelo acesso e persistência dos dados no banco de dados.                             |
| `database/`   | Contém os recursos responsáveis pela conexão, inicialização e comunicação com o banco de dados. |
| `config/`     | Centraliza configurações gerais da aplicação.                                                   |

### Comunicação entre as camadas

* **Controller → Service:** encaminha as solicitações para as regras de negócio.
* **Service → Model:** utiliza as entidades do domínio durante o processamento das operações.
* **Service → DAO:** solicita consultas, inserções, alterações e exclusões de dados.
* **DAO → Database:** realiza as operações de persistência no banco de dados.
* **Enums:** são utilizados pelas entidades e regras de negócio para representar valores controlados.

