# Estrutura

Para entender melhor a organização do projeto **Task Tracker**, veja a estrutura de diretórios detalhada abaixo. Este projeto segue a arquitetura **MVC (Model-View-Controller)**, garantindo uma separação clara de responsabilidades e facilitando a manutenção e escalabilidade da aplicação.

## Estrutura de Diretórios

```
.
├── .mvn
│   └── wrapper
│       └── maven-wrapper.properties
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.randre.task_tracker
│   │   │       ├── TaskTrackerApplication.java
│   │   │       ├── constants/
│   │   │       ├── controllers/
│   │   │       ├── dtos
│   │   │       │   ├── jwt
│   │   │       │   │   ├── AccessResponseDTO.java
│   │   │       │   │   ├── LoginResponseDTO.java
│   │   │       │   │   └── RefreshRequestDTO.java
│   │   │       │   ├──task
│   │   │       │   │   ├── TaskPaginationDTO.java
│   │   │       │   │   ├── TaskRequestDTO.java
│   │   │       │   │   ├── TaskResponseDTO.java
│   │   │       │   │   └── TaskUpdateDTO.java
│   │   │       │   └── user
│   │   │       │       ├── UserRequestDTO.java
│   │   │       │       └── UserResponseDTO.java
│   │   │       ├── exceptions/
│   │   │       ├── infrastructure
│   │   │       │   ├── config/
│   │   │       │   ├── enums/
│   │   │       │   ├── errors/
│   │   │       │   ├── filters/
│   │   │       │   └── security/
│   │   │       ├── mappers/
│   │   │       ├── models/
│   │   │       ├── repositories/
│   │   │       ├── services/
│   │   │       ├── utils/
│   │   │       └── validations
│   │   │           ├── annotations/
│   │   │           └── validators/
│   │   │       
│   │   └── resources
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── application-test.properties
│   │       ├── application.properties
│   │       └── internationalization
│   │           ├── messages.properties
│   │           ├── messages_en.properties
│   │           └── messages_pt_BR.properties
│   └── test
│       └── java.com.randre.task_tracker
│           ├── TaskTrackerApplicationTests.java
│           ├── controllers/
│           ├── infrastructure/
│           └── services/
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
└── render.yaml
```

## 📁 Descrição dos Diretórios e Arquivos

### Raiz do Projeto

- **`.dockerignore`**: Arquivo que especifica quais arquivos ou diretórios devem ser ignorados pelo Docker durante o processo de build. Ajuda a reduzir o tamanho da imagem Docker.

- **`.gitattributes`**: Define atributos específicos para arquivos no repositório Git, como tratamento de finais de linha.

- **`.gitignore`**: Especifica quais arquivos ou pastas o Git deve ignorar. Evita que arquivos sensíveis ou desnecessários sejam versionados.

- **`.mvn` & `maven-wrapper.properties`**: Configurações do Maven Wrapper, permitindo que o projeto seja construído com uma versão específica do Maven, independentemente da versão instalada localmente.

- **`Dockerfile`**: Script para construir a imagem Docker da aplicação. Define o ambiente de execução, copia o código fonte, instala dependências e configura o ponto de entrada da aplicação.

- **`compose.yml`**: Configuração do Docker Compose para orquestrar múltiplos containers (como a aplicação e o banco de dados PostgreSQL).

- **`mvnw` & `mvnw.cmd`**: Scripts do Maven Wrapper para rodar comandos Maven sem necessidade de instalação prévia do Maven na máquina local.

- **`pom.xml`**: Arquivo de configuração do Maven que define as dependências, plugins e outras configurações de build do projeto.

- **`render.yaml`**: Configurações específicas para deploy no Render, como serviços, builds e env variables.

### `src/main/java/com/randre/task_tracker`

#### `TaskTrackerApplication.java`
Ponto de entrada da aplicação Spring Boot. Inicializa o contexto do Spring e configurações principais.

#### `constants`
Contém classes que definem constantes utilizadas na aplicação, como mensagens de erro e constantes de segurança.

- **`ErrorMessages.java`**: Define mensagens de erro padrão para reutilização em todo o projeto.
- **`SecurityConstants.java`**: Define constantes relacionadas à segurança, como prefixos de token.

#### `controllers`
Camada **Controller** da arquitetura MVC. Responsável por lidar com as requisições HTTP, delegando processos para os serviços e retornando respostas.

- **`AuthController.java`**: Gerencia endpoints de autenticação (registro, login, refresh de tokens).
- **`TaskController.java`**: Gerencia endpoints relacionados a tarefas (CRUD, busca, paginação).

#### `dtos`
Objetos de Transferência de Dados (DTOs) utilizados para comunicação entre as camadas da aplicação, garantindo que apenas os dados necessários sejam expostos.

- **`jwt`**: DTOs específicos para operações de autenticação JWT.
    - **`AccessResponseDTO.java`**
    - **`LoginResponseDTO.java`**
    - **`RefreshRequestDTO.java`**
- **`task`**: DTOs para operações relacionadas a tarefas.
    - **`TaskPaginationDTO.java`**
    - **`TaskRequestDTO.java`**
    - **`TaskResponseDTO.java`**
    - **`TaskUpdateDTO.java`**
- **`user`**: DTOs para operações relacionadas a usuários.
    - **`UserRequestDTO.java`**
    - **`UserResponseDTO.java`**

#### `exceptions`
Contém classes de exceção personalizadas que representam erros específicos da aplicação, facilitando o tratamento de erros e a manutenção.

- **`AccessDeniedException.java`**
- **`TaskNotFoundException.java`**
- **`UserAlreadyExistsException.java`**
- **`UserNotFoundException.java`**

#### `infrastructure`
Camada de infraestrutura que suporta funcionalidades transversais, como configuração, segurança e tratamento de erros.

- **`config`**: Configurações gerais da aplicação.
    - **`CustomLocaleResolver.java`**: Configurações para resolução de localidade (idioma).
    - **`InternationalizationConfig.java`**: Configurações para internacionalização da aplicação.
    - **`OpenAPIConfig.java`**: Configurações para geração da documentação OpenAPI (Swagger).

- **`enums`**: Enumerações utilizadas na aplicação.
    - **`Permission.java`**
    - **`Priority.java`**
    - **`Role.java`**

- **`errors`**:
    - **`RestExceptionHandler.java`**: Manipulador global de exceções REST, garantindo respostas padronizadas.
    - **`RestExceptionMessage.java`**: Formato padrão para mensagens de erro nas respostas.

- **`filters`**:
    - **`FilterTaskAuth.java`**: Filtros específicos para autenticação e autorização nas requisições de tarefas.
    - **`SecurityFilter.java`**: Filtro geral de segurança para interceptar e validar tokens JWT.

- **`security`**:
    - **`SecurityConfig.java`**: Configurações de segurança da aplicação, definindo políticas de acesso, autenticação e autorização.

#### `mappers`
Responsáveis por mapear entidades e DTOs, facilitando a conversão entre diferentes representações de dados.

- **`TaskMapper.java`**
- **`UserMapper.java`**

#### `models`
Entidades que representam os modelos de dados no banco de dados, seguindo o padrão de nomenclatura e relacionamento.

- **`TaskModel.java`**
- **`UserModel.java`**

#### `repositories`
Interfaces de repositório que estendem as funcionalidades do Spring Data JPA, permitindo operações de persistência nos modelos.

- **`ITaskRepository.java`**
- **`IUserRepository.java`**

#### `services`
Camada de **Service** da arquitetura MVC. Contém a lógica de negócios da aplicação, manipulando dados e interagindo com os repositórios.

- **`AuthService.java`**: Lida com a lógica de autenticação e registro de usuários.
- **`JwtService.java`**: Gerencia a criação e validação de tokens JWT.
- **`MessageService.java`**: Serviço para gestão de mensagens internas e notificações.
- **`TaskService.java`**: Gerencia operações relacionadas a tarefas, como criação, atualização e deleção.

#### `utils`
Classes utilitárias que fornecem funcionalidades auxiliares para diferentes partes da aplicação.

- **`Utils.java`**: Métodos utilitários gerais, como formatação de dados e validações.

#### `validations`
Validações personalizadas para garantir a integridade e consistência dos dados recebidos pela API.

- **`annotations`**: Anotações customizadas para validações específicas.
    - **`DateRange.java`**
    - **`FutureDate.java`**
    - **`NullOrNotBlank.java`**

- **`validators`**: Implementações das validações customizadas.
    - **`DateRangeValidator.java`**
    - **`FutureDateValidator.java`**
    - **`NullOrNotBlankValidator.java`**

### `src/main/resources`

- **`application.properties`**: Configurações gerais da aplicação Spring Boot.
- **`application-dev.properties`**, **`application-prod.properties`**, **`application-test.properties`**: Configurações específicas por perfil (desenvolvimento, produção, teste).
- **`internationalization`**: Arquivos de propriedades para suporte a múltiplos idiomas.
    - **`messages.properties`**: Mensagens padrão.
    - **`messages_en.properties`**: Mensagens em inglês.
    - **`messages_pt_BR.properties`**: Mensagens em português do Brasil.

### `src/test/java/com/randre/task_tracker`

Contém os testes automatizados da aplicação, garantindo a qualidade e a corretude do código.

- **`TaskTrackerApplicationTests.java`**: Testes de integração gerais da aplicação.

#### `controllers`
Testes específicos para os controllers da aplicação.

- **`AuthControllerTest.java`**
- **`TaskControllerTest.java`**

#### `infrastructure/errors`
Testes para o manipulador global de exceções.

- **`RestExceptionHandlerTest.java`**

#### `services`
Testes para os serviços da aplicação, verificando a lógica de negócios.

- **`AuthServiceIntegrationTest.java`**
- **`AuthServiceTest.java`**
- **`JwtServiceTest.java`**
- **`TaskServiceTest.java`**

## 🔍 Decisões Técnicas

- **Arquitetura MVC**: Implementada para separar responsabilidades, facilitando a manutenção e escalabilidade.
- **Spring Security com JWT**: Escolhida para garantir uma autenticação robusta e escalável, permitindo o uso de tokens de acesso e refresh tokens.
- **Lombok**: Utilizado para reduzir o boilerplate de código, como getters e setters, aumentando a produtividade.
- **HATEOAS**: Implementado para facilitar a navegação entre recursos da API, seguindo os princípios RESTful.
- **Docker e Docker Compose**: Facilitaram o processo de containerização e orquestração de serviços, garantindo ambientes consistentes de desenvolvimento e produção.
- **Banco de Dados H2 e PostgreSQL**: H2 utilizado para facilitar testes e desenvolvimento local com um banco de dados em memória, enquanto PostgreSQL assegura robustez e performance em produção.
- **Testes Automatizados**: Garantem a qualidade do código e previnem regressões, facilitando a manutenção e evolução da aplicação.
- **Internacionalização**: Suporte a múltiplos idiomas para atender a uma base de usuários diversificada.

Essa estrutura organizada e as decisões técnicas adotadas visam criar uma aplicação robusta, segura e fácil de manter, seguindo as melhores práticas de desenvolvimento de software.