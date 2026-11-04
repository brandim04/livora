# Livora — Sistema de Gerenciamento de Leituras

O **Livora** é uma aplicação web desenvolvida para auxiliar leitores na organização e no acompanhamento de suas leituras. O sistema permite cadastrar livros, organizar uma estante virtual, acompanhar o progresso de leitura e consultar informações sobre os livros cadastrados.

O projeto foi desenvolvido como atividade acadêmica do curso de Engenharia de Software, utilizando tecnologias modernas de desenvolvimento frontend e backend.

## Acesse o sistema

**[Clique aqui para acessar o Livora](https://brandim04.github.io/livora/)**

O sistema está publicado no GitHub Pages e pode ser acessado diretamente pelo navegador.

> Observação: a versão publicada disponibiliza a interface do sistema. As funcionalidades que dependem da API necessitam que o backend e o banco de dados estejam hospedados e configurados para acesso externo.

## Tecnologias utilizadas

### Frontend
- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- Axios
- Lucide Icons

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL 17
- Swagger / OpenAPI

## Funcionalidades

- Cadastro, edição, consulta e exclusão de livros.
- Listagem e organização dos livros cadastrados.
- Busca e filtros por título, autor e gênero.
- Gerenciamento de leituras.
- Acompanhamento do progresso de leitura.
- Atualização do status de leitura.
- Registro de avaliações.
- Validação de dados e regras de negócio.
- Documentação dos endpoints da API com Swagger.

## Arquitetura do projeto

O backend foi organizado seguindo o padrão de arquitetura em camadas, separando as responsabilidades da aplicação.

| Camada | Responsabilidade |
|---|---|
| Controller | Receber as requisições HTTP e retornar as respostas. |
| Service | Implementar as regras de negócio. |
| Repository | Realizar a comunicação com o banco de dados. |
| Model | Representar as entidades do sistema. |
| DTO | Transportar os dados entre as camadas. |
| Exception | Centralizar o tratamento de exceções. |

## Estrutura do projeto

```text
livora/
├── frontend/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── router/
│   │   ├── services/
│   │   ├── stores/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.ts
│   ├── public/
│   ├── package.json
│   └── vite.config.ts
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/livora/livora/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── exception/
│       │       ├── model/
│       │       ├── repository/
│       │       └── service/
│       └── resources/
│
├── .github/
│   └── workflows/
│       └── deploy.yml
│
├── .gitignore
├── pom.xml
└── README.md
```

## Modelagem do banco de dados

O sistema possui duas entidades principais:

### Livro

Representa os livros cadastrados na aplicação, armazenando informações como título, autor, gênero e ISBN, além de outros dados necessários para sua identificação e organização.

### Leitura

Representa o acompanhamento da leitura de um livro, armazenando informações como status, progresso, datas e avaliação.

### Relacionamento entre as entidades

Um livro pode possuir vários registros de leitura, enquanto cada registro de leitura está associado a um único livro.

O relacionamento foi implementado utilizando as anotações:

- `@OneToMany` na entidade Livro.
- `@ManyToOne` na entidade Leitura.

## Regras de negócio

O sistema implementa regras para garantir a consistência dos dados e o funcionamento correto das operações.

1. **ISBN único:** não é permitido cadastrar livros com ISBN duplicado.
2. **Avaliação:** a avaliação deve respeitar o intervalo de 0 a 5, com incrementos de 0,5, e só pode ser informada quando a leitura estiver com status LIDO.
3. **Leitura ativa:** não é permitido manter mais de uma leitura ativa para o mesmo livro.
4. **Validação de datas:** as datas informadas devem respeitar as regras estabelecidas pelo sistema.
5. **Progresso de leitura:** o progresso não pode ser negativo, ultrapassar a quantidade de páginas do livro ou diminuir em relação ao progresso anterior.
6. **Conclusão automática:** ao atingir a última página do livro, a leitura pode ser marcada automaticamente como concluída.

## Validações e tratamento de exceções

O backend utiliza validações para impedir o envio de informações inválidas.

Entre os recursos utilizados estão:

- `@Valid` para validar os dados recebidos.
- Anotações de validação do Bean Validation.
- `ResponseEntity` para controlar as respostas HTTP.
- `@RestControllerAdvice` para centralizar o tratamento de exceções.
- Exceções personalizadas para situações como conflitos e recursos não encontrados.

As principais exceções personalizadas são:

- `ConflitoException`
- `RecursoNaoEncontradoException`

## API REST

A API foi desenvolvida utilizando Spring Boot e segue os princípios REST, disponibilizando endpoints para gerenciamento dos livros e das leituras.

### Livros

| Operação | Método HTTP | Descrição |
|---|---|---|
| Cadastrar | POST | Cadastra um novo livro. |
| Listar | GET | Retorna os livros cadastrados. |
| Buscar por ID | GET | Consulta um livro específico. |
| Atualizar | PUT | Atualiza os dados de um livro. |
| Excluir | DELETE | Remove um livro. |
| Filtrar | GET | Pesquisa livros por título, autor ou gênero. |

### Leituras

| Operação | Método HTTP | Descrição |
|---|---|---|
| Cadastrar | POST | Registra uma nova leitura. |
| Listar | GET | Retorna as leituras cadastradas. |
| Buscar por ID | GET | Consulta uma leitura específica. |
| Atualizar | PUT | Atualiza os dados de uma leitura. |
| Excluir | DELETE | Remove um registro de leitura. |
| Filtrar | GET | Consulta leituras por status. |

## Documentação da API — Swagger

A documentação dos endpoints foi implementada utilizando Swagger / OpenAPI.

Com o backend em execução, a documentação pode ser acessada em:

`http://localhost:8080/swagger-ui/index.html`

Por meio dela, é possível visualizar os endpoints, consultar os parâmetros, verificar os formatos de requisição e resposta e testar as operações da API.

## Requisitos acadêmicos atendidos

| Requisito | Implementação no projeto |
|---|---|
| CRUD completo | Operações de cadastro, consulta, atualização e exclusão de livros e leituras. |
| Filtros | Consultas por título, autor, gênero e status de leitura. |
| Arquitetura em camadas | Separação em Controller, Service e Repository. |
| Relacionamento entre tabelas | Relacionamento `@OneToMany` e `@ManyToOne` entre Livro e Leitura. |
| DTOs | Utilização de objetos de transferência de dados. |
| ResponseEntity | Utilização nos controllers para estruturar as respostas HTTP. |
| Validações | Bean Validation e validações específicas das regras de negócio. |
| Tratamento de exceções | `@RestControllerAdvice` e exceções personalizadas. |
| Regras de negócio | Validação de ISBN, progresso, avaliação, datas e leituras ativas. |
| Swagger | Documentação dos endpoints da API. |

## Publicação

O frontend está publicado por meio do **GitHub Pages**, com processo de integração e publicação automatizado pelo GitHub Actions.

A cada atualização enviada para a branch `master`, o workflow realiza a instalação das dependências, gera a versão de produção do frontend e publica os arquivos gerados.

**Acesse:** https://brandim04.github.io/livora/

## Autoria

Projeto desenvolvido como atividade acadêmica do curso de Engenharia de Software do Centro Universitário Santo Agostinho (UNIFSA).

**Desenvolvedora:** Larissa Brandim

**Repositório:** https://github.com/brandim04/livora
