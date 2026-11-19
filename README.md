# Java Alunos

Este projeto tem dois principais motivos para a sua existência:

- Compara-lo com uma versão análoga em Rust, para determinar qual que seria a melhor escolha
- Colocar como trabalho de desenvolvimento WEB

Ele consiste em um sistema backend de trabalhar com notas para com os alunos, relacionando alunos, turmas, professor, periodos, materias e donos.

## Como usa-lo?
Faça o clone desse repositorio:
```shell
git clone https://github.com/pedro-makoski/java-alunos.git
```

Depois, prossiga com a execução (você precisara do Docker instalado para essa execução):
```shell
docker compose build --no-cache
```

E por fim, rodar o sistema
```shell
docker compose up
```

O sistema rodará na porta 8080, por padrão.

## Adendo Importante
Esse projeto não tem o comprotimento de escalar para um caso real. Portanto, ele não possui sistema de caching, logs, segundos fatores e entre outros.

## Requisitos para rodar o sistema
- Apenas o Docker
- Atualmente só foi testado no sistema operacional Ubuntu, porém dificilmente não funcionará em outros

## Rotas do Sistema

Esta seção documenta todos os endpoints da API **Java Scholls**, agrupados por recurso. A URL base em ambiente local é `http://localhost:8080`.

**Convenções gerais**

- Todos os identificadores (`id`, `idTurma`, `idAluno` etc.) são **UUID**.
- Corpos de requisição são enviados em `application/json`.
- Listagens são **paginadas** e retornam um objeto `Page` (ver [Estrutura de paginação](#estrutura-de-paginação)).
- Todas as rotas documentadas retornam `200 OK` em caso de sucesso.

---

### Usuário (`user-controller`)

### `GET /user/me`
Retorna os dados principais do usuário para verificar se está logado.

**Resposta:**

| Campo       | Tipo   | Descrição            |
|-------------|--------|----------------------|
| `nome`      | string | Nome do usuário      |
| `sobrenome` | string | Sobrenome do usuário |


#### `POST /user/login`
Autentica um usuário.

**Corpo da requisição (`LoginRequest`)**

| Campo | Tipo | Obrigatório |
|---|---|---|
| `email` | string | - |
| `senha` | string | - |

**Resposta (`LoginResponse`)**

| Campo | Tipo | Descrição |
|---|---|---|
| `statusMsg` | string | Mensagem de status do login |
| `idUser` | uuid | Identificador do usuário |

---

#### `POST /user/create-account`
Cria uma nova conta de usuário.

**Corpo da requisição (`CreateAccountRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `email` | string | Sim | Formato de e-mail |
| `senha` | string | Sim | 8 a 250 caracteres |
| `dataNascimento` | date | Sim | `yyyy-MM-dd` |
| `nome` | string | Sim | 2 a 250 caracteres |
| `sobrenome` | string | Sim | 2 a 250 caracteres |

**Resposta:** `LoginResponse` (`statusMsg`, `idUser`).

---

#### `DELETE /user/deslogin`
Encerra a sessão do usuário (logout).

**Resposta:** objeto vazio.

---

### Turmas (`turma-controller`)

#### `POST /turma/create`
Cria uma turma.

**Corpo da requisição (`CreateTurmaRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `nome` | string | Sim | 3 a 250 caracteres |
| `tipoTurma` | enum | Sim | `SUPERIOR`, `MEDIO`, `FUNDAMENTAL`, `INFANTIL` |
| `tamanhoMaximo` | int64 | Sim | Mínimo 0 |

**Resposta (`CreateTurmaResponse`):** `{ "idTurma": "uuid" }`

---

#### `GET /turma`
Lista as turmas do usuário, com paginação e filtro por nome.

**Query params**

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `nome` | string | `""` | Filtro por nome |
| `pageNumber` | int32 | `0` | Número da página |

**Resposta:** `PageSampleTurma`

---

#### `GET /turma/{id}`
Busca uma turma pelo ID.

**Path param:** `id` (uuid)

**Resposta (`SampleTurma`)**

| Campo | Tipo |
|---|---|
| `id` | uuid |
| `nome` | string |
| `tipoTurma` | enum |
| `tamanhoMaximoTurma` | int64 |

---

#### `PUT /turma/{id}`
Atualiza uma turma.

**Path param:** `id` (uuid)

**Corpo (`PutTurmaRequest`):** mesmos campos e validações de `CreateTurmaRequest` (`nome`, `tipoTurma`, `tamanhoMaximo`, todos obrigatórios).

---

### Professores (`professor-controller`)

#### `POST /professor/create`
Cadastra um professor.

**Corpo da requisição (`CreateProfessorRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `email` | string | Sim | - |
| `senha` | string | Sim | Mínimo 8 caracteres |
| `nome` | string | Sim | - |
| `sobrenome` | string | Sim | - |
| `dataNascimento` | date | Sim | `yyyy-MM-dd` |

**Resposta (`CreateProfessorResponse`):** `{ "professorId": "uuid" }`

---

#### `GET /professor`
Lista os professores do usuário.

**Query params**

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `term` | string | `""` | Termo de busca |
| `pageNumber` | int32 | `0` | Número da página |

**Resposta:** `PageSampleProfessor`

---

#### `GET /professor/{id}`
Busca um professor pelo ID.

**Resposta (`SampleProfessor`)**

| Campo | Tipo |
|---|---|
| `idProfessor` | uuid |
| `nome` | string |
| `sobrenome` | string |
| `dataNascimento` | date |
| `email` | string |

---

#### `PUT /professor/{id}`
Atualiza um professor.

**Corpo (`PutProfessorRequest`)**

| Campo | Tipo | Obrigatório |
|---|---|---|
| `nome` | string | Sim |
| `sobrenome` | string | Sim |
| `dataNascimento` | date | Sim |

---

#### `DELETE /professor/{id}`
Remove um professor.

---

### Períodos (`periodo-controller`)

#### `POST /periodo/create`
Cria um período letivo.

**Corpo da requisição (`CreatePeriodoRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `tipoPeriodo` | enum | Sim | `SEMESTRAL`, `ANUAL`, `TRIMESTRAL`, `BIMESTRAL` |
| `inicio` | date | Sim | `yyyy-MM-dd` |
| `fim` | date | Sim | `yyyy-MM-dd` |

**Resposta (`CreatePeriodoResponse`):** `{ "idPeriodo": "uuid" }`

---

#### `GET /periodo`
Lista os períodos do usuário.

**Query params:** `pageNumber` (int32, padrão `0`)

**Resposta:** `PagePeriodoSample`

---

#### `GET /periodo/{id}`
Busca um período pelo ID.

**Resposta (`PeriodoSample`):** `idPeriodo`, `inicio`, `fim`, `tipoPeriodo`.

---

#### `PUT /periodo/{id}`
Atualiza um período.

**Corpo (`PutPeriodoRequest`):** `tipoPeriodo`, `inicio`, `fim` (todos obrigatórios).

---

#### `DELETE /periodo/{id}`
Remove um período.

---

#### `PATCH /periodo/add-turma-in-periodo/{periodoId}`
Associa uma turma a um período.

| Parâmetro | Local | Tipo | Obrigatório |
|---|---|---|---|
| `periodoId` | path | uuid | Sim |
| `turmaId` | query | uuid | Sim |

---

### Matérias (`materia-controller`)

#### `POST /materia/create`
Cria uma matéria.

**Corpo da requisição (`CreateMateriaRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `nome` | string | Sim | 2 a 250 caracteres |
| `professorId` | uuid | Sim | - |
| `turmaId` | uuid | Sim | - |

**Resposta (`CreateMateriaResponse`):** `{ "materiaId": "uuid" }`

---

#### `GET /materia`
Lista as matérias do usuário.

**Query params**

| Parâmetro | Tipo | Padrão |
|---|---|---|
| `term` | string | `""` |
| `pageNumber` | int32 | `0` |

**Resposta:** `PageMateriaSample`

---

#### `GET /materia/{id}`
Busca uma matéria pelo ID.

**Resposta (`MateriaSample`):** `idMateria`, `nome`, `sampleProfessor` (`SampleProfessor`), `sampleTurma` (`SampleTurma`).

---

#### `PUT /materia/{id}`
Atualiza uma matéria.

**Corpo (`PutMateriaRequest`):** `nome`, `professorId`, `turmaId` (todos obrigatórios).

---

#### `DELETE /materia/{id}`
Remove uma matéria.

---

#### `GET /materia/materias-of-aluno/{alunoId}`
Lista as matérias de um aluno.

| Parâmetro | Local | Tipo | Padrão |
|---|---|---|---|
| `alunoId` | path | uuid | - |
| `pageNumber` | query | int32 | `0` |

**Resposta:** `PageMateriaSample`

---

#### `GET /materia/grid-materias/get`
Retorna a grade de notas de uma matéria em um período.

| Parâmetro | Local | Tipo | Obrigatório |
|---|---|---|---|
| `periodoId` | query | uuid | Sim |
| `materiaId` | query | uuid | Sim |

**Resposta (`GridNotas`)**

```json
{
  "userItemList": [
    {
      "id": "uuid",
      "nome": "string",
      "nota": 0.0,
      "detailsNota": [{ "description": "string", "nota": 0.0 }]
    }
  ]
}
```

---

#### `GET /materia/grid-materia/get-periodos`
Lista os períodos em que uma matéria está associada.

| Parâmetro | Local | Tipo | Padrão | Obrigatório |
|---|---|---|---|---|
| `idMateria` | query | uuid | - | Sim |
| `pageNumber` | query | int32 | `0` | Não |

**Resposta:** `PagePeriodoSample`

---

#### `PATCH /materia/change-turma-materia/{materiaId}`
Altera a turma de uma matéria.

| Parâmetro | Local | Tipo | Obrigatório |
|---|---|---|---|
| `materiaId` | path | uuid | Sim |
| `turmaId` | query | uuid | Sim |

---

#### `PATCH /materia/add-periodo-in-materia/{materiaId}`
Associa um período a uma matéria.

| Parâmetro | Local | Tipo | Obrigatório |
|---|---|---|---|
| `materiaId` | path | uuid | Sim |
| `periodoId` | query | uuid | Sim |

---

### Alunos (`aluno-controller`)

#### `POST /aluno/create`
Cadastra um aluno.

**Corpo da requisição (`CreateAlunoRequest`)**

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `idTurma` | uuid | Sim | - |
| `nome` | string | Sim | 2 a 250 caracteres |
| `dataNascimento` | date | Sim | `yyyy-MM-dd` |
| `nomePai` | string | Sim | 2 a 250 caracteres |
| `nomeMae` | string | Sim | 2 a 250 caracteres |

**Resposta (`CreateAlunoResponse`):** `{ "idAluno": "uuid" }`

---

#### `GET /aluno`
Lista os alunos do usuário.

**Query params**

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `pageNumber` | int32 | `0` | Número da página |
| `searchTerm` | string | `""` | Termo de busca |

**Resposta:** `PageAlunoSample`

---

#### `GET /aluno/{id}`
Busca um aluno pelo ID.

**Resposta (`AlunoSample`):** `idAluno`, `nome`, `nomePai`, `nomeMae`, `dataNascimento`.

---

#### `PUT /aluno/{id}`
Atualiza um aluno.

**Corpo (`PutAlunoRequest`):** `nome`, `dataNascimento`, `nomePai`, `nomeMae` (todos obrigatórios, com as mesmas validações da criação).

---

#### `DELETE /aluno/{id}`
Remove um aluno.

---

#### `GET /aluno/overview/{id}`
Retorna o resumo de desempenho de um aluno.

**Resposta (`AlunoDetails`)**

| Campo | Tipo | Descrição |
|---|---|---|
| `idAluno` | uuid | ID do aluno |
| `nomeAluno` | string | Nome do aluno |
| `notaMedia` | double | Média geral |
| `materiaAndPeriodoNotas` | map<string, double> | Notas por matéria/período |

---

#### `PATCH /aluno/turma/change`
Altera a turma de um aluno.

| Parâmetro | Local | Tipo | Obrigatório |
|---|---|---|---|
| `alunoId` | query | uuid | Sim |
| `turmaId` | query | uuid | Sim |

---

#### `POST /aluno/nota-aluno/create`
Lança a nota de um aluno em uma matéria e período.

**Corpo da requisição (`AddNotaRequest`)**

| Campo | Tipo | Validação |
|---|---|---|
| `nota` | double | 0 a 100 |
| `detailsNota` | array de `DetailsNota` | Detalhamento da nota |
| `idAluno` | uuid | - |
| `idPeriodo` | uuid | - |
| `materiaId` | uuid | - |

**`DetailsNota`**

| Campo | Tipo | Validação |
|---|---|---|
| `description` | string | - |
| `nota` | double | 0 a 100 |

---

#### `GET /aluno/nota-aluno/details`
Consulta a nota de um aluno em uma matéria e período.

| Parâmetro | Tipo | Obrigatório |
|---|---|---|
| `idAluno` | uuid | Sim |
| `idMateria` | uuid | Sim |
| `idPeriodo` | uuid | Sim |

**Resposta (`GetNotaAlunoResponse`):** `nota` (double) e `detailsNota` (array de `DetailsNota`).

---

### Estrutura de paginação

Todas as rotas de listagem retornam um objeto `Page` com a seguinte estrutura (o campo `content` varia conforme o recurso):

| Campo | Tipo | Descrição |
|---|---|---|
| `content` | array | Itens da página atual |
| `number` | int32 | Número da página atual (inicia em 0) |
| `size` | int32 | Tamanho da página |
| `numberOfElements` | int32 | Quantidade de itens na página atual |
| `totalElements` | int64 | Total de itens |
| `totalPages` | int32 | Total de páginas |
| `first` / `last` | boolean | Indica primeira/última página |
| `empty` | boolean | Indica página vazia |
| `sort` | objeto | `empty`, `sorted`, `unsorted` |
| `pageable` | objeto | `offset`, `pageNumber`, `pageSize`, `paged`, `unpaged`, `sort` |

---

### Resumo das rotas

| Método | Rota | Descrição |
|---|---|---|
| POST | `/user/login` | Login |
| POST | `/user/create-account` | Criar conta |
| DELETE | `/user/deslogin` | Logout |
| POST | `/turma/create` | Criar turma |
| GET | `/turma` | Listar turmas |
| GET | `/turma/{id}` | Buscar turma |
| PUT | `/turma/{id}` | Atualizar turma |
| POST | `/professor/create` | Criar professor |
| GET | `/professor` | Listar professores |
| GET | `/professor/{id}` | Buscar professor |
| PUT | `/professor/{id}` | Atualizar professor |
| DELETE | `/professor/{id}` | Remover professor |
| POST | `/periodo/create` | Criar período |
| GET | `/periodo` | Listar períodos |
| GET | `/periodo/{id}` | Buscar período |
| PUT | `/periodo/{id}` | Atualizar período |
| DELETE | `/periodo/{id}` | Remover período |
| PATCH | `/periodo/add-turma-in-periodo/{periodoId}` | Adicionar turma ao período |
| POST | `/materia/create` | Criar matéria |
| GET | `/materia` | Listar matérias |
| GET | `/materia/{id}` | Buscar matéria |
| PUT | `/materia/{id}` | Atualizar matéria |
| DELETE | `/materia/{id}` | Remover matéria |
| GET | `/materia/materias-of-aluno/{alunoId}` | Matérias de um aluno |
| GET | `/materia/grid-materias/get` | Grade de notas da matéria |
| GET | `/materia/grid-materia/get-periodos` | Períodos de uma matéria |
| PATCH | `/materia/change-turma-materia/{materiaId}` | Trocar turma da matéria |
| PATCH | `/materia/add-periodo-in-materia/{materiaId}` | Adicionar período à matéria |
| POST | `/aluno/create` | Criar aluno |
| GET | `/aluno` | Listar alunos |
| GET | `/aluno/{id}` | Buscar aluno |
| PUT | `/aluno/{id}` | Atualizar aluno |
| DELETE | `/aluno/{id}` | Remover aluno |
| GET | `/aluno/overview/{id}` | Resumo do aluno |
| PATCH | `/aluno/turma/change` | Trocar turma do aluno |
| POST | `/aluno/nota-aluno/create` | Lançar nota |
| GET | `/aluno/nota-aluno/details` | Consultar nota |