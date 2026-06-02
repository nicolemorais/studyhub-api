# StudyHub API — Sistema de Gestão de Salas de Estudo Virtuais

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/projects/jdk21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x_/_4.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-Central-blueviolet?logo=apachemaven)](https://maven.apache.org/)

O **StudyHub** é uma API REST desenvolvida para prover um ecossistema estruturado de colaboração pedagógica síncrona e assíncrona entre professores e alunos. 

---

## Tecnologias

* **Runtime:** Java 21.0.6 
* **Framework Core:** Spring Boot
* **Persistência & Banco de Dados:** PostgreSQL & Spring Data JPA
* **Build Tool:** Maven (Empacotamento otimizado em `.jar`)
* **Testes Automatizados:** JUnit 5, Mockito e AssertJ 
* **Testes de Integração & Contratos:** Postman

---

## Escopo do Projeto

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
