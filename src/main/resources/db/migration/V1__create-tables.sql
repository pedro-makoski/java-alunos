create table users (
    id  uuid not null,
    email varchar(100) unique not null,
    senha text not null,
    data_nascimento date,
    nome varchar(100),
    sobrenome varchar(100),

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id)
);

create table turma (
    id uuid not null,
    nome varchar(250),
    tipo varchar(20),
    tamanho_maximo int,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id),
    CONSTRAINT chk_tipo CHECK (tipo IN ('SUPERIOR', 'MEDIO', 'FUNDAMENTAL', 'INFANTIL'))
);

create table aluno (
    id uuid not null,
    nome varchar(250),
    data_nascimento date,
    nome_pai varchar(250),
    nome_mae varchar(250),
    id_turma uuid,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id),
    CONSTRAINT fork_aluno_turma FOREIGN KEY (id_turma) references turma(id)
);

create table periodo (
    id  uuid not null,
    tipo varchar(30),
    inicio date,
    fim date,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id),
    constraint check_tipo_periodo
            check (tipo in ('SEMESTRAL', 'ANUAL', 'TRIMESTRAL', 'BIMESTRAL'))
);

create table turma_periodo (
    turma_id uuid not null,
    periodo_id uuid not null,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (turma_id, periodo_id),
    constraint turma_periodo_ids_turma foreign key (turma_id) references turma(id),
    constraint turma_periodo_ids_periodo foreign key (periodo_id) references periodo(id)
);

create table professor (
    id uuid not null,
    email varchar(250) unique,
    senha text,
    nome varchar(250),
    dono uuid not null,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id),
    constraint dono_professor foreign key (dono) references users(id)
);

create table materia (
    id uuid not null,
    nome varchar(250),
    professor uuid,
    turma uuid,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (id),
    constraint professor_materia foreign key (professor) references professor(id),
    constraint turma_materia foreign key (turma) references turma(id)
);

create table periodo_materia (
    materia_id uuid not null,
    periodo_id uuid not null,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (materia_id, periodo_id),
    constraint periodo_materia_periodo foreign key (periodo_id) references periodo(id),
    constraint periodo_materia_materia foreign key (materia_id) references materia(id)
);

create table nota_aluno_periodo_materia (
    nota double precision,
    aluno_id uuid not null,
    periodo_id uuid not null,
    materia_id uuid not null,
    details jsonb,

    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_deleted boolean,
    deleted_at TIMESTAMP WITH TIME ZONE,

    primary key (aluno_id, periodo_id, materia_id),
    constraint nota_aluno foreign key (aluno_id) references aluno(id),
    constraint nota_periodo foreign key (periodo_id) references periodo(id),
    constraint nota_materia foreign key (materia_id) references materia(id)
);