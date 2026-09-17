# Sistema de Gestão da Biblioteca Municipal

Sistema informático de consola, desenvolvido em **Java**, para apoiar os bibliotecários na gestão do inventário de livros, dos utilizadores registados e do histórico de empréstimos/devoluções. Foi desenvolvido no âmbito da unidade curricular de Programação, no curso de Licenciatura em Engenharia Informática da **UnISCED (Universidade Aberta ISCED)**.

> Repositório público: **`<https://github.com/Rjofrice/Biblioteca-Municipal>`**

---

## Índice

1. [Descrição do projeto](#-descrição-do-projeto)
2. [Funcionalidades](#-funcionalidades)
3. [Arquitetura e estrutura de dados](#-arquitetura-e-estrutura-de-dados)
4. [Estrutura do repositório](#-estrutura-do-repositório)
5. [Requisitos / Dependências](#-requisitos--dependências)
6. [Configuração e execução](#-configuração-e-execução)
7. [Como usar (exemplo de sessão)](#-como-usar-exemplo-de-sessão)
8. [Testes realizados](#-testes-realizados)
9. [Limitações conhecidas](#-limitações-conhecidas)
10. [Trabalho futuro](#-trabalho-futuro)
11. [Autor](#-autor)
12. [Licença](#-licença)

---

## Descrição do projeto

Com o crescimento do número de leitores, a Biblioteca Municipal necessitava de uma ferramenta simples e fiável para automatizar tarefas antes feitas manualmente: registar livros, controlar quantos exemplares estão disponíveis, saber quem tem cada livro emprestado e produzir estatísticas de utilização. Este programa responde a essa necessidade através de um **menu interativo em consola**, sem dependências externas, que mantém uma base de dados simulada **em memória** (usando vetores/arrays e uma matriz), reiniciada sempre que o programa é executado.

## Funcionalidades

| # | Funcionalidade | Descrição |
|---|---|---|
| 1 | **Registo de Livros** | Insere novos títulos no catálogo com identificador único gerado automaticamente, título, autor, ano de publicação e quantidade de exemplares disponíveis. |
| 2 | **Registo de Utilizadores** | Regista leitores da biblioteca (nome e email) com identificador único. |
| 3 | **Consulta de Catálogo** | Lista todos os livros existentes, com o respetivo estado de disponibilidade. |
| 4 | **Pesquisa** | Pesquisa livros por título ou por autor (correspondência parcial, sem distinção de maiúsculas/minúsculas). |
| 5 | **Empréstimo** | Associa um livro a um utilizador registado, diminuindo a quantidade de exemplares disponíveis e impedindo o empréstimo quando não há stock. |
| 6 | **Devolução** | Regista a devolução de um exemplar, repondo-o no acervo disponível. |
| 7 | **Estatísticas** | Mostra o livro mais requisitado da biblioteca, o total histórico de empréstimos e o número de empréstimos atualmente em curso. |
| 8 | **Listagem de Utilizadores** | Lista todos os utilizadores registados no sistema. |

## Arquitetura e estrutura de dados

O sistema segue uma organização simples em **camada única (console + lógica de negócio)**, dividida nas seguintes classes Java (pacote `biblioteca`):

- **`Livro`** — modelo de dados de um livro (id, título, autor, ano, quantidade total/disponível, nº de vezes emprestado).
- **`Utilizador`** — modelo de dados de um utilizador da biblioteca (id, nome, email).
- **`Emprestimo`** — regista uma operação de empréstimo/devolução (id, id do livro, id do utilizador, data de empréstimo, data de devolução, estado).
- **`Biblioteca`** — núcleo do sistema; mantém a "base de dados" em memória e implementa toda a lógica de negócio.
- **`Main`** — ponto de entrada da aplicação; apresenta o menu interativo em consola e trata a interação com o utilizador.

Conforme pedido, a informação **não é guardada em coleções da *Java Collections Framework*** (`ArrayList`, `HashMap`, etc.), mas sim em:

- **Vetores/arrays** dinâmicos (`Livro[]`, `Utilizador[]`, `Emprestimo[]`), redimensionados manualmente (duplicando a capacidade) sempre que ficam cheios — implementação clássica de um *array dinâmico*.
- Uma **matriz de inteiros** `int[matrizEmprestimosAtivos][ ]`, de dimensão `utilizadores × livros`, em que a posição `[i][j]` guarda o número de empréstimos atualmente ativos entre o utilizador de índice `i` e o livro de índice `j`. Esta matriz é atualizada em cada empréstimo/devolução e permite, por exemplo, verificar rapidamente se um determinado utilizador tem um exemplar de um determinado livro em seu poder.

## Estrutura do repositório

```
biblioteca-municipal/
├── README.md
├── docs/
│   └── (documentação técnica, capturas de ecrã)
├── src/
│   └── main/
│       └── java/
│           └── biblioteca/
│               ├── Main.java          # Menu interativo (ponto de entrada)
│               ├── Biblioteca.java    # Lógica de negócio + base de dados em memória
│               ├── Livro.java         # Modelo: Livro
│               ├── Utilizador.java    # Modelo: Utilizador
│               └── Emprestimo.java    # Modelo: Registo de empréstimo
└── .gitignore
```

## Requisitos / Dependências

- **Java Development Kit (JDK) 17 ou superior** (o projeto usa *switch expressions*, disponíveis desde o Java 14/17 LTS).
- Não são necessárias bibliotecas externas nem gestor de dependências (Maven/Gradle) — o projeto compila apenas com as ferramentas padrão do JDK (`javac` / `java`).
- Sistema operativo: qualquer um com JDK instalado (Windows, Linux ou macOS).

Para verificar se tem o Java instalado e a versão:

```bash
java -version
javac -version
```

## Configuração e execução

### 1. Clonar o repositório

```bash
git clone <https://github.com/Rjofrice/Biblioteca-Municipal>
cd biblioteca-municipal
```

### 2. Compilar o projeto

```bash
javac -d bin src/main/java/biblioteca/*.java
```

Este comando compila todas as classes e coloca os ficheiros `.class` na pasta `bin/`.

### 3. Executar o programa

```bash
java -cp bin biblioteca.Main
```

> Se os acentos aparecerem trocados na consola (ex.: Windows `cmd`), execute com a codificação UTF-8 explícita:
> ```bash
> java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp bin biblioteca.Main
> ```
> ou, no `cmd` do Windows, corra antes `chcp 65001`.

### 4. Utilizar o menu

Ao arrancar, o sistema já vem com alguns **dados de demonstração** pré-carregados (3 livros e 2 utilizadores), para facilitar os testes. A partir daí, basta seguir as opções numeradas do menu.

## Como usar (exemplo de sessão)

```
=================================================
   BIBLIOTECA MUNICIPAL - SISTEMA DE GESTÃO
=================================================
 1. Registar novo livro
 2. Registar novo utilizador
 3. Listar catálogo completo
 4. Pesquisar livro (título/autor)
 5. Efetuar empréstimo
 6. Efetuar devolução
 7. Ver estatísticas
 8. Listar utilizadores registados
 0. Sair
=================================================
Escolha uma opção: 5

--- Empréstimo de Livro ---
ID do utilizador: 1
ID do livro: 1
Empréstimo registado com sucesso!
```

Mais exemplos, com capturas de ecrã comentadas de todas as funcionalidades, estão disponíveis na **documentação técnica** (pasta `docs/`).

## Testes realizados

O programa foi testado manualmente, percorrendo todos os itens do menu com cenários de sucesso e de erro, nomeadamente:

- Registo de livros e utilizadores com dados válidos.
- Pesquisa por título e por autor, com e sem resultados.
- Empréstimo com sucesso, empréstimo sem exemplares disponíveis e empréstimo para utilizador/livro inexistente.
- Devolução com sucesso e devolução sem empréstimo ativo correspondente.
- Verificação das estatísticas (livro mais emprestado e totais) após múltiplas operações.
- Introdução de valores não numéricos nos campos numéricos, validando a rejeição e nova solicitação do valor.

## Limitações conhecidas

- Os dados residem apenas em memória: **ao fechar o programa, toda a informação é perdida** (não existe persistência em ficheiro ou base de dados).
- Não existe autenticação de bibliotecários (o sistema assume um único operador de confiança por sessão).
- A capacidade dos vetores é ajustada automaticamente (duplicando), pelo que não há um limite prático de registos além da memória disponível.

## Trabalho futuro

- Persistência dos dados em ficheiro (texto/CSV) ou base de dados relacional.
- Interface gráfica (Swing/JavaFX) ou API REST.
- Datas limite de devolução e cálculo de multas por atraso.
- Autenticação de bibliotecários com diferentes níveis de permissão.

## Autor

Trabalho realizado no âmbito da disciplina de Programação — Licenciatura em Engenharia Informática, Faculdade de Engenharia e Agricultura, UnISCED (Universidade Aberta ISCED), pelo estudante Rivaldo Conde Jofrice.

## Licença

Projeto disponibilizado para fins académicos, ao abrigo da licença MIT (ver ficheiro `LICENSE`, se aplicável).
