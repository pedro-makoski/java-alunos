alter table aluno add column dono uuid;

alter table aluno add constraint dono_aluno foreign key (dono) references users(id);