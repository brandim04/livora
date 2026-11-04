# Livora — Sistema de Gerenciamento de Leituras

O **Livora** é uma aplicação web desenvolvida para auxiliar leitores na organização de seus livros e no acompanhamento de suas leituras. A proposta é oferecer uma biblioteca virtual na qual seja possível pesquisar livros, organizar uma estante pessoal e acompanhar o progresso de leitura.

Projeto desenvolvido como atividade acadêmica do curso de Engenharia de Software do Centro Universitário Santo Agostinho (UNIFSA).

> **Status: Em desenvolvimento.**
>
> O Livora ainda não está finalizado. Esta versão contempla apenas parte das funcionalidades planejadas. O projeto está em evolução, e novas funcionalidades e melhorias serão implementadas nas próximas versões.

## Acesse o sistema

**[Acessar Livora](https://brandim04.github.io/livora/)**

O frontend está publicado no GitHub Pages e pode ser acessado diretamente pelo navegador, sem necessidade de baixar o código-fonte.

> **Importante:** a publicação atual disponibiliza o frontend. As funcionalidades que dependem da API própria do Livora precisam de backend e banco de dados hospedados para funcionar para usuários externos.

---

## Demonstração do sistema

Abaixo serão apresentados registros visuais das telas e funcionalidades já desenvolvidas.

### Página inicial

![Página inicial do Livora](docs/images/tela-inicial.png)

### Explorar livros

![Tela de exploração de livros](docs/images/explorar-livros.png)

### Pesquisa de livros

![Resultados da pesquisa de livros](docs/images/pesquisa-livros.png)

### Cadastro de livros

![Tela de cadastro de livros](docs/images/cadastro-livro.png)

### Estante virtual

![Estante virtual do Livora](docs/images/estante.png)

---

## Funcionalidades implementadas

Esta versão contempla parte da estrutura e das funcionalidades planejadas:

- Interface visual desenvolvida com Vue 3.
- Navegação entre páginas utilizando Vue Router.
- Estrutura inicial da biblioteca virtual.
- Tela de exploração e pesquisa de livros.
- Integração com a Open Library API.
- Consulta de informações bibliográficas.
- Exibição de resultados de pesquisa.
- Estrutura inicial do backend com Spring Boot.
- CRUD de livros e leituras na API própria.
- Filtros para consulta de livros.
- Modelagem das entidades Livro e Leitura.
- Relacionamento entre as entidades.
- Validações e regras de negócio.
- Documentação dos endpoints com Swagger.

Nem todas as funcionalidades estão completamente integradas à versão publicada.

---

## Integração com a Open Library API

Um dos recursos do Livora é a integração com a **Open Library API**, serviço externo que disponibiliza informações bibliográficas de livros.

A integração permite pesquisar obras pelo título e obter informações que auxiliam no preenchimento do cadastro, reduzindo a necessidade de inserir todos os dados manualmente.

### Como funciona?

1. O usuário informa o título do livro que deseja pesquisar.
2. O backend recebe a solicitação.
3. O sistema realiza uma consulta à Open Library API.
4. A API retorna os registros encontrados.
5. O Livora organiza e apresenta os resultados.
6. Os dados selecionados podem ser utilizados como referência para o cadastro do livro.

### API utilizada

**Open Library Search API**

Endpoint externo:

`https://openlibrary.org/search.json`

Parâmetros utilizados:

| Parâmetro | Finalidade |
|---|---|
| `title` | Pesquisar pelo título do livro. |
| `fields` | Definir quais informações serão retornadas. |
| `limit` | Limitar a quantidade de resultados. |

Entre os campos consultados estão:

| Campo | Informação |
|---|---|
| `title` | Título da obra. |
| `author_name` | Nome dos autores. |
| `isbn` | ISBN disponível. |
| `publisher` | Editora. |
| `first_publish_year` | Ano da primeira publicação. |
| `cover_i` | Identificador da capa. |
| `number_of_pages_median` | Quantidade mediana de páginas. |
| `subject` | Assuntos relacionados ao livro. |

### Exibição das capas

As capas são obtidas por meio do serviço de imagens da Open Library:

`https://covers.openlibrary.org/b/id/{cover_id}-M.jpg`

**Documentação oficial:** https://openlibrary.org/developers/api

> Os dados retornados dependem dos registros disponíveis na Open Library. Algumas informações podem não estar disponíveis em todos os resultados.

---

## Tecnologias utilizadas

### Frontend

| Tecnologia | Utilização |
|---|---|
| Vue 3 | Construção da interface. |
| TypeScript | Tipagem e organização do código. |
| Vite | Ambiente de desenvolvimento e build. |
| Vue Router | Gerenciamento de rotas. |
| Pinia | Gerenciamento de estado. |
| Axios | Comunicação com APIs. |
| Lucide Icons | Ícones da interface. |

### Backend

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem de programação. |
| Spring Boot | Desenvolvimento da API REST. |
| Spring Data JPA | Persistência de dados. |
| Hibernate | Mapeamento objeto-relacional. |
| Maven | Gerenciamento de dependências. |
| PostgreSQL 17 | Banco de dados relacional. |
| Swagger / OpenAPI | Documentação da API. |

### Serviços externos

- Open Library API: consulta de informações bibliográficas e capas.
- GitHub Pages: publicação do frontend.
- GitHub Actions: automação do processo de publicação.

---

## Arquitetura do projeto

O backend segue uma arquitetura em camadas, separando as responsabilidades da aplicação.

| Camada | Responsabilidade |
|---|---|
| Controller | Receber requisições HTTP e retornar respostas. |
| Service | Implementar regras de negócio e integrações externas. |
| Repository | Realizar operações no banco de dados. |
| Model | Representar as entidades do sistema. |
| DTO | Transportar dados entre as camadas. |
| Exception | Centralizar o tratamento de exceções. |

## Estrutura do projeto

```text
livora/
├── .github/
│   └── workflows/
│       └── deploy.yml
├── docs/
│   └── images/
│       ├── tela-inicial.png
│       ├── explorar-livros.png
│       ├── pesquisa-livros.png
│       ├── cadastro-livro.png
│       └── estante.png
├── frontend/
│   └── src/
│       ├── assets/
│       ├── components/
│       ├── router/
│       ├── services/
│       ├── stores/
│       └── views/
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
├── .gitignore
├── pom.xml
└── README.md
```

---

## Modelagem do banco de dados

O sistema possui duas entidades principais:

### Livro

Representa os livros cadastrados, armazenando informações como título, autor, gênero, ISBN, editora, ano de publicação e quantidade de páginas.

### Leitura

Representa o acompanhamento da leitura de um livro, armazenando informações como status, progresso, datas e avaliação.

### Relacionamento entre entidades

Um livro pode possuir vários registros de leitura, enquanto cada registro de leitura está associado a um único livro.

O relacionamento foi implementado utilizando:

- `@OneToMany` na entidade Livro.
- `@ManyToOne` na entidade Leitura.

---

## API REST

O backend utiliza Spring Boot para disponibilizar endpoints REST responsáveis pelo gerenciamento dos livros e das leituras.

### Livros

| Método | Operação |
|---|---|
| POST | Cadastrar livro. |
| GET | Listar livros. |
| GET | Consultar livro por ID. |
| PUT | Atualizar livro. |
| DELETE | Excluir livro. |
| GET | Filtrar livros por título, autor ou gênero. |

### Leituras

| Método | Operação |
|---|---|
| POST | Registrar leitura. |
| GET | Listar leituras. |
| GET | Consultar leitura por ID. |
| PUT | Atualizar leitura. |
| DELETE | Excluir leitura. |
| GET | Filtrar leituras por status. |

### Documentação Swagger

Com o backend em execução, a documentação pode ser acessada em:

`http://localhost:8080/swagger-ui/index.html`

---

## Regras de negócio

O backend possui regras para garantir a consistência das informações:

1. **ISBN único:** não é permitido cadastrar livros com ISBN duplicado.
2. **Avaliação:** deve respeitar o intervalo de 0 a 5, com incrementos de 0,5, e só pode ser informada quando a leitura estiver com status LIDO.
3. **Leitura ativa:** não é permitido manter mais de uma leitura ativa para o mesmo livro.
4. **Validação de datas:** as datas devem respeitar as condições estabelecidas pelo sistema.
5. **Progresso:** não pode ser negativo, ultrapassar a quantidade de páginas do livro ou diminuir em relação ao progresso anterior.
6. **Conclusão automática:** ao atingir a última página, a leitura pode ser marcada automaticamente como concluída.

---

## Validações e tratamento de exceções

O backend utiliza:

- `@Valid` e Bean Validation.
- `ResponseEntity` para estruturar respostas HTTP.
- `@RestControllerAdvice` para tratamento global de exceções.
- Exceções personalizadas para conflitos e recursos não encontrados.

Exceções implementadas:

- `ConflitoException`
- `RecursoNaoEncontradoException`

---

## Próximas versões — V2

A segunda versão terá como objetivo ampliar a experiência do usuário, melhorar a organização da biblioteca e implementar recursos que tornem o Livora mais completo.

### 1. Gerenciamento da biblioteca

- Finalizar a integração entre frontend e backend.
- Permitir adicionar e remover livros da estante.
- Implementar edição das informações dos livros.
- Melhorar a organização e visualização da biblioteca.
- Implementar filtros avançados.

### 2. Acompanhamento de leituras

- Criar uma experiência completa de acompanhamento.
- Registrar início e término de cada leitura.
- Atualizar o progresso de páginas lidas.
- Criar histórico de leituras concluídas.
- Permitir avaliações e comentários pessoais.

### 3. Perfil do usuário

- Implementar cadastro e autenticação.
- Criar perfil personalizado.
- Permitir que cada usuário tenha sua própria biblioteca.
- Associar livros e registros de leitura ao usuário autenticado.

### 4. Estatísticas e metas

- Criar painel de estatísticas.
- Exibir quantidade de livros lidos.
- Acompanhar páginas lidas.
- Criar metas mensais e anuais.
- Apresentar gráficos de evolução.

### 5. Experiência e interface

- Aprimorar a responsividade.
- Melhorar a experiência em dispositivos móveis.
- Adicionar animações e interações.
- Aprimorar a pesquisa e os filtros.
- Criar recomendações de livros.

### 6. Infraestrutura e publicação

- Hospedar o backend em um serviço externo.
- Hospedar o banco de dados PostgreSQL.
- Configurar variáveis de ambiente para produção.
- Integrar o frontend publicado com a API hospedada.
- Melhorar a segurança e o gerenciamento de erros.

---

## Requisitos acadêmicos atendidos

| Requisito | Implementação |
|---|---|
| CRUD completo | Operações de cadastro, consulta, atualização e exclusão de livros e leituras. |
| Filtros | Consultas por título, autor, gênero e status. |
| Arquitetura em camadas | Separação em Controller, Service e Repository. |
| Relacionamento entre tabelas | `@OneToMany` e `@ManyToOne` entre Livro e Leitura. |
| DTOs | Objetos de transferência de dados. |
| ResponseEntity | Respostas HTTP estruturadas nos controllers. |
| Validações | Bean Validation e regras específicas de negócio. |
| Tratamento de exceções | `@RestControllerAdvice` e exceções personalizadas. |
| Regras de negócio | Validações de ISBN, progresso, avaliação, datas e leituras ativas. |
| Swagger | Documentação dos endpoints da API. |

---

## Publicação e versionamento

O frontend está publicado por meio do GitHub Pages.

O processo de publicação é automatizado com GitHub Actions. Quando há alterações enviadas para a branch `master`, o workflow realiza:

1. Checkout do repositório.
2. Configuração do Node.js.
3. Instalação das dependências.
4. Geração do build de produção.
5. Publicação dos arquivos no GitHub Pages.

**Acesse:** https://brandim04.github.io/livora/

---

## Autoria

Projeto desenvolvido por **Larissa Brandim**, como atividade acadêmica do curso de Engenharia de Software do Centro Universitário Santo Agostinho (UNIFSA).

**GitHub:** https://github.com/brandim04

**Repositório:** https://github.com/brandim04/livora
