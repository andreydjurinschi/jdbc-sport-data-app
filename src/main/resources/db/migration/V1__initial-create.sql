create table league (
    id bigserial primary key,
    name varchar(100) not null
);

create table team (
    id serial primary key,
    name varchar(100) not null,
    league_id int not null,
    foreign key (league_id) references league(id)
);

create table coach (
    id serial primary key,
    name varchar(100) not null,
    team_id int unique,
    foreign key (team_id) references team(id)
);

create table player (
    id serial primary key,
    name varchar(100) not null,
    position varchar(50),
    number int,
    team_id int not null,
    foreign key (team_id) references team(id)
);

create table match (
    id serial primary key,
    match_date date not null,
    home_team_id int not null,
    away_team_id int not null,
    league_id int not null,
    foreign key (home_team_id) references team(id),
    foreign key (away_team_id) references team(id),
    foreign key (league_id) references league(id)
);

create table match_result (
    id serial primary key,
    match_id int unique not null,
    home_score int not null,
    away_score int not null,
    foreign key (match_id) references match(id)
);
