# DESOFS-Projeto — Secure Software Development Life Cycle (SSDLC) Project

## Contexto

Este repositório corresponde ao projeto desenvolvido no âmbito da unidade curricular de **Desenvolvimento de Software Seguro (DESOFS)** do Mestrado em Engenharia Informática (2024/2025). O objetivo principal é aplicar, de forma sistemática, o ciclo de vida de desenvolvimento seguro de software (SSDLC) a todo o projeto, desde a análise de requisitos até ao deployment, com ênfase na identificação, mitigação e controlo de ameaças.

## Propósito do Sistema

O sistema desenvolvido tem como objetivo a Gestão de faturas/dívidas, abrangendo as funcionalidades essenciais ao seu domínio.

## Estrutura do Projeto

O desenvolvimento é realizado em duas fases principais:
- **Fase 1: Análise e Desenho**
  - Requisitos funcionais e não funcionais (incluindo requisitos de segurança)
  - Abuse cases
  - Modelação de ameaças (Threat Modeling: STRIDE, DFDs, análise de riscos)
  - Arquitetura e design seguros
  - Planeamento de testes de segurança
- **Fase 2: Implementação, Testes e Deployment**
  - Desenvolvimento incremental (2 sprints)
  - DevSecOps: pipeline com integração de práticas de segurança (SAST, DAST, IAST, SCA)
  - Testes automatizados (unitários, integração, segurança)
  - Code reviews, auditorias, validação de requisitos de segurança
  - Hardening, logging, configuração segura, gestão de dependências
  - Avaliação contínua via ASVS (OWASP Application Security Verification Standard)

## Funcionalidades Obligatorias

- **Backend API** RESTful (desenvolvida em Java Spring Boot)
- **Base de dados relacional** PostgreSQL
- **DDD (Domain-Driven Design)**: pelo menos 3 agregados (incluindo utilizadores)
- **Autorização**: pelo menos três perfis/roles distintos
- **Execução de operações do sistema operativo** (criação de diretórios, leitura/escrita de ficheiros, etc.)

## Práticas SSDLC, Segurança e DevSecOps

- **Threat Modeling**: STRIDE, abuse cases, data flow diagrams (DFD) com trust boundaries
- **Requirements Traceability**: requisitos de segurança ligados a ameaças e testes
- **Risk Assessment**: metodologia estruturada para priorizar riscos
- **Mitigação**: soluções claras para ameaças identificadas, com foco em prioridades elevadas
- **DevSecOps Pipeline**: 
  - **CI/CD**: integração de ferramentas como SonarQube, Snyk/OWASP Dependency-Check, Zaproxy, etc.
  - **Testes**: unitários, integração, SAST, DAST, IAST, SCA, cobertura, mutação
  - **Code reviews** e práticas de secure coding
  - **Automatização**: scripts para builds, deploy, análise e rollback
- **Documentação e Evidências**:
  - Entregáveis organizados em `Deliverables/`, incluindo checklists ASVS por fase/sprint
  - Relatórios de threat modeling, requisitos, arquitetura, testes e evidências de automação
  - Referências e links para código e documentação relevante

## Organização do Repositório

- `src/` — Código-fonte da aplicação
- `Deliverables/` — Documentação de cada fase/sprint, ASVS checklist e evidências
- `docs/` — Diagramas, relatórios técnicos detalhados, threat models, DFDs, etc.
- `Jenkinsfile`/`.github/workflows/` — Pipelines CI/CD e scripts de DevSecOps
- `README.md` — Este ficheiro

## Tecnologias e Ferramentas

- **Backend**: [Spring Boot, Node.js, etc.]
- **Base de Dados**: [PostgreSQL, MySQL, etc.]
- **CI/CD**: GitHub Actions
- **SAST/DAST/IAST**: SonarQube, OWASP ZAP, Snyk, Dependency-Check
- **Testes**: JUnit, Jest, PIT, Postman/Newman, REST-assured
- **DevSecOps**: Docker, Docker Compose, scripts bash/powershell
- **ASVS**: OWASP Application Security Verification Standard checklist

## Avaliação

O projeto é avaliado em duas fases principais:
- **Moment 1 (M1)**: Threat Modeling, análise e design seguro
- **Moment 2 (M2)**: Implementação, testes, pipeline DevSecOps, segurança e entrega contínua




