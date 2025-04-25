create table coach (
id serial primary key,
name varchar(100) not null,
team_id int unique,
foreign key (team_id) references team(id)
);