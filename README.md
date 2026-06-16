# StudyHub API — Sistema de Gestão de Salas de Estudo Virtuais

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/projects/jdk21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-Central-blueviolet?logo=apachemaven)](https://maven.apache.org/)

O **StudyHub** é uma API REST desenvolvida para prover um ecossistema estruturado de colaboração pedagógica síncrona e assíncrona entre professores e alunos.

---

## Escopo do projeto

O ecossistema é segregado em fluxos operacionais baseados na governança de perfis de acesso:

### Governança do professor

* **Salas virtuais:** Endpoints restritos para provisionamento e depreciação de ambientes colaborativos por disciplina.
* **Roteiros pedagógicos:** Criação e manutenção de guias de estudo estruturados de forma granular em tópicos e materiais didáticos de apoio.
* **Desafios semanais:** Mecanismo de alimentação prática de exercícios com pontuação fixa atrelada às diretrizes da plataforma.
* **Atendimentos síncronos:** Agendamento de plantões ao vivo com gatilho de ativação temporal automatizado pelo relógio do servidor.

### Interação do aluno

* **Comunicação instantânea:** Chat assíncrono e síncrono integrado para debater tópicos e mitigar o isolamento de alunos dispersos.
* **Compartilhamento de recursos:** Upload de mídias de apoio diretamente via fluxo de mensagens.
* **Mecanismo de busca indexada:** Filtro performático e paginado de histórico textual por palavras-chave.
* **Engine de gamificação:** Monitoramento em tempo real de progressão com processamento e concessão automatizada de insígnias corporativas baseadas em metas alcançadas.

## Instruções de execução local

### Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

* **Java JDK 17** ou superior
* **Maven 3.8+**
* **PostgreSQL 14+** (ou um container Docker ativo)

### Configuração do ambiente (`.env`)

A API utiliza variáveis de ambiente para proteger dados sensíveis. Crie um arquivo `.env` na raiz do projeto (use o `.env.template` como base) e configure as suas credenciais:

``` properties
DB_URL=jdbc:postgresql://localhost:5432/studyhub_db
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres
JWT_SECRET=sua_chave
```

### Comandos para compilar e rodar o projeto

Abra o terminal na raiz do projeto e execute os passos abaixo:

1. Limpar builds antigos e baixar as dependências:

    ``` pwsh
    mvn clean
    ```

2. Compilar a aplicação e rodar os testes automatizados:

    ``` pwsh
    mvn clean package
    ```

3. Executar a aplicação via plugin do Spring Boot:

    ``` pwsh
    mvn spring-boot:run
    ```

### Obtenção e uso do Token JWT

O ecossistema de rotas (exceto cadastro e login) é protegido. Siga os passos para se autenticar:

#### Passo 1: Criar uma conta

Envie uma requisição para criar o seu perfil (ex: PROFESSOR ou ALUNO):

* **POST** `http://localhost:8080/api/v1/auth/cadastro`
* **Body (JSON):**

    ``` json
    {
    "nome": "Professor",
    "email": "prof@ifsp.edu.br",
    "senha": "SenhaTeste@2026",
    "perfil": "PROFESSOR"
    }
    ```

#### Passo 2: Efetuar o login

* **POST** `http://localhost:8080/api/v1/auth/login`
* **Body (JSON): Envie o e-mail e senha cadastrados.**
* **Resposta da API: Você receberá um JSON contendo o token.**

    ``` json
    {
    "token": "eyJhbGciO..."
    }
    ```

#### Passo 3: Utilizar o Token nas requisições protegidas

* Em qualquer rota protegida (como criar salas ou guias), você deve incluir o token no cabeçalho HTTP da requisição utilizando o formato **Bearer Token**.

    ``` http
    Authorization: Bearer <cole_o_token_aqui>
    ```

### Formas de Testar a API

#### Via Postman (Recomendado)

* Abra o Postman.
* Crie uma nova requisição informando a URL desejada (`Ex: GET <http://localhost:8080/api/v1/salas>`).
* Vá até a aba *Authorization*, selecione o tipo *Bearer Token* e cole o token gerado no login.
* Clique em **Send**.

#### Via HTTPie (Terminal)

Se você prefere testar direto pela linha de comando usando o HTTPie, utilize os exemplos:

* Login:
  
  ``` pwsh
  http POST http://localhost:8080/api/v1/auth/login email="professor@ifsp.edu.br" senha="SenhaTeste@2026"
  ```

* Listar salas (enviando o Token):

  ``` pwsh
  http GET http://localhost:8080/api/v1/salas "Authorization:Bearer eyJhbGciO..."
  ```
