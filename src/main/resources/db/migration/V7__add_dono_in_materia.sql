alter table materia add column dono uuid;

alter table materia add constraint dono_materia foreign key (dono) references users(id);