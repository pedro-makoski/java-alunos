alter table periodo add column dono uuid;

alter table periodo add constraint dono_periodo foreign key (dono) references users(id);