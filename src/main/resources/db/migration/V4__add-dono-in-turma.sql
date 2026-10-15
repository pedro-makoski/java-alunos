alter table turma add column dono;

alter table turma add constraint dono_turma foreign key (dono) references users(id);