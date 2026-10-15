alter table turma add column dono uuid;

alter table turma add constraint dono_turma foreign key (dono) references users(id);