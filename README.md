
# Projeto: Dashboard de Gerenciamento de Projetos

## Descrição

Este projeto é um **Dashboard de Gerenciamento de Projetos** desenvolvido com **HTML**, **JavaScript**, **Bootstrap** no frontend, e integrado a um backend utilizando **Spring Boot**, **Hibernate** e **JPA**. Ele permite criar, editar, visualizar e gerenciar projetos e membros da equipe de forma simples e eficiente, além de classificar os projetos de acordo com o risco orçamentário.

## Funcionalidades

### 1. Gerenciamento de Projetos
- **Criação, edição e remoção de projetos.**
- **Classificação de risco** baseada no orçamento:
  - **Baixo Risco:** Orçamento abaixo de R$ 100.000
  - **Médio Risco:** Orçamento entre R$ 100.000 e R$ 500.000
  - **Alto Risco:** Orçamento acima de R$ 500.000
- **Acompanhamento do status dos projetos**, como "Planejado", "Em Análise", "Em Andamento", entre outros.
  
### 2. Gerenciamento de Membros
- **Cadastro de membros** como funcionários ou gerentes.
- Associação de membros a projetos.
- Visualização e remoção de membros dos projetos.

### 3. Integração com WebService
O projeto interage com uma API REST desenvolvida em **Spring Boot** para realizar as operações de CRUD. Abaixo estão alguns exemplos das rotas disponíveis:

- **Pessoas**
  - `GET /pessoas` - Lista todas as pessoas cadastradas.
  - `POST /pessoas` - Cria uma nova pessoa.
  - `PUT /pessoas/{id}` - Edita uma pessoa existente.
  - `DELETE /pessoas/{id}` - Remove uma pessoa.

- **Projetos**
  - `GET /projetos` - Lista todos os projetos.
  - `POST /projetos` - Cria um novo projeto.
  - `PUT /projetos/{id}` - Edita um projeto existente.
  - `DELETE /projetos/{id}` - Remove um projeto.

Para mais detalhes, consulte o código-fonte no arquivo [api.js](./api.js).

## Estrutura do frontend

- `index.html` - Interface gráfica do usuário, construída com HTML e Bootstrap, que exibe a lista de projetos e membros, além de formulários modais para edição e criação.
- `api.js` - Módulo responsável por todas as interações com a API REST, implementando operações como criação, edição, exclusão e consulta de projetos e pessoas.
- `index.js` - Código JavaScript responsável por manipular a DOM, carregar dados da API e gerenciar a lógica de exibição e manipulação de projetos e membros.

## Tecnologias Utilizadas

- **HTML5**
- **JavaScript (ES6+)**
- **Bootstrap 5**
- **FontAwesome** - Ícones utilizados no dashboard.
- **Spring Boot** - Back-end RESTful.
- **Hibernate & JPA** - ORM e persistência de dados.
- **Prism.js** - Biblioteca para realce de código.

## Como Rodar o Projeto

### Pré-requisitos
- **Spring Boot** configurado e rodando na porta `8080`.
- O projeto deve estar configurado para servir arquivos estáticos da pasta `static`, onde os arquivos **index.html**, **api.js** e **index.js** estão localizados.

