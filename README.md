# Task Tracker

![Licença MIT](https://img.shields.io/github/license/ricardoandreh/task-tracker-api)
![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-green)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red)

## Descrição

**Task Tracker** é uma API de gerenciamento de tarefas simples que permite aos usuários criar, organizar e acompanhar suas atividades diárias. Desenvolvida com Java Spring Boot, a aplicação oferece funcionalidades robustas de autenticação, autorização, paginação, pesquisa e muito mais, facilitando o gerenciamento eficiente de tarefas em projetos pessoais ou colaborativos.

## 🚀 Funcionalidades

- **Autenticação JWT**: Suporte para tokens de acesso e refresh tokens.
- **Gerenciamento de Tarefas**: Criação, atualização, deleção e busca de tarefas.
- **Paginação e Pesquisa**: Facilita a navegação e localização de tarefas específicas.
- **Segurança com Spring Security**: Controle de acesso baseado em funções de usuário.
- **Hypermedia HATEOAS**: Navegação intuitiva entre recursos da API.
- **Suporte a Múltiplos Bancos de Dados**: H2 (em memória) para desenvolvimento e PostgreSQL para produção.
- **Docker e Docker Compose**: Facilita o deploy e a configuração de ambientes.
- **Deploy no Render**: Implementação simplificada em ambiente de produção.

## 🛠 Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Framework**: Spring Boot
- **Gerenciador de Dependências**: Maven
- **Segurança**: Spring Security
- **Ferramentas de Produtividade**: Lombok
- **Arquitetura de API**: HATEOAS
- **Autenticação**: JWT (Access e Refresh Tokens)
- **Banco de Dados**: H2 (Desenvolvimento), PostgreSQL (Produção)
- **Containerização**: Docker, Docker Compose
- **Deploy**: Render

## 📚 Endpoints

### Autenticação

| Método | Endpoint         | Descrição                        |
| ------ | ---------------- |----------------------------------|
| POST   | `/auth/register` | Registro de novos usuários       |
| POST   | `/auth/login`    | Login e obtenção de tokens JWT   |
| POST   | `/auth/refresh`  | Renovação de tokens JWT          |

### Tarefas

| Método | Endpoint                | Descrição                                 |
| ------ | ----------------------- |-------------------------------------------|
| GET    | `/tasks`                | Listar todas as tarefas com paginação     |
| GET    | `/tasks/{id}`           | Obter detalhes de uma tarefa específica   |
| GET    | `/tasks/search`         | Buscar tarefas com parâmetros específicos |
| POST   | `/tasks`                | Criar uma nova tarefa                     |
| PATCH  | `/tasks/{id}`           | Atualizar uma tarefa existente            |
| DELETE | `/tasks/{id}`           | Deletar uma tarefa                        |

### Swagger

A documentação interativa da API está disponível no endpoint `/swagger-ui/index.html` e no redirecionador `/docs` após a execução da aplicação.

## 🔧 Configuração do Ambiente

### 📋 Variáveis de Ambiente

| Nome                     | Valor Padrão                 | Descrição                                |
|--------------------------|------------------------------|------------------------------------------|
| `RENDER_EXTERNAL_URL`    | (sem valor padrão)           | URL externa do Render para deploy        |
| `SECRET_KEY`             | `my-secret-key`              | Chave secreta para JWT                   |
| `DATABASE_USER`          | (definido no Docker Compose) | Usuário do banco de dados                |
| `DATABASE_PASSWORD`      | (definido no Docker Compose) | Senha do banco de dados                  |
| `DATABASE_HOST`          | `postgres`                   | Host do banco de dados                   |
| `DATABASE_PORT`          | `5432`                       | Porta do banco de dados                  |
| `DATABASE_NAME`          | `task_tracker`               | Nome do banco de dados                   |
| `JDK_JAVA_OPTIONS`       | `-Xms256m -Xmx512m`          | Opções de configuração da JVM            |
| `SPRING_PROFILES_ACTIVE` | `prod`                       | Perfil ativo do Spring (dev, prod, test) |

### 📦 Docker e Docker Compose

A aplicação utiliza Docker para containerização e Docker Compose para orquestração de múltiplos containers (API e banco de dados). Certifique-se de que o Docker está instalado em sua máquina.

#### Build e Run com Docker Compose

```sh
docker-compose up --build
```

Este comando irá construir as imagens necessárias e iniciar os containers definidos no `docker-compose.yml`.

## 🏃‍♂️ Como Rodar a Aplicação

### 🔧 Usando Maven

1. **Clonar o Repositório**

   ```sh
   git clone https://github.com/ricardoandreh/task-tracker-api.git
   cd task-tracker-api
   ```

2. **Instalar Dependências**

   ```sh
   ./mvnw install
   ```

3. **Executar a Aplicação**

   Para rodar em modo desenvolvimento:

   ```sh
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   Para rodar em modo produção:

   ```sh
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
   ```

### 🐳 Usando Docker

1. **Build da Imagem Docker**

   ```sh
   docker build -t task-tracker-api .
   ```

2. **Executar o Container**

   ```sh
   docker run -d -p 10000:10000 --name task-tracker-api task-tracker-api
   ```

### 🚀 Deploy no Render

Siga as instruções na plataforma [Render](https://render.com/) para realizar o deploy da aplicação utilizando o `Dockerfile` ou conectando diretamente ao repositório GitHub.

## 🧪 Testes

A aplicação possui uma suite de testes automatizados para garantir a qualidade e a integridade do código.

### Executar Testes

```sh
./mvnw test
```

## 📂 Estrutura do Projeto

Para uma descrição detalhada da estrutura de pastas e a arquitetura MVC adotada, consulte o arquivo [`STRUCTURE.md`](./STRUCTURE.md).

## 📝 Licença

Este projeto está licenciado sob os termos da licença [MIT](https://github.com/ricardoandreh/task-tracker-api/blob/master/LICENSE).
