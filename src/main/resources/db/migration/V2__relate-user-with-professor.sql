alter table professor add column user_related uuid;
alter table professor add constraint user_teacher_id foreign key (user_related)
        references users(id);

alter table professor drop column nome;
alter table professor drop column email;
alter table professor drop column senha;