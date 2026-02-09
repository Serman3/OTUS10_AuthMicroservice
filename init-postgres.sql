create table t_user
(
    id       serial primary key,
    username varchar not null unique,
    password varchar not null
);

create table t_user_authority
(
    id          serial primary key,
    id_user     int     not null references t_user (id),
    c_authority varchar not null,
    unique (id_user, c_authority)
);

create table t_deactivated_token
(
    id           uuid primary key,
    c_keep_until timestamp not null check ( c_keep_until > now() )
);

insert into t_user(username, password) values ('j.jameson', '{noop}password');

insert into t_user_authority(id_user, c_authority) values (1, 'ROLE_MANAGER');

create table t_game
(
    id           serial primary key,
    id_game      varchar not null unique,
    created_date timestamp not null default now()
);

create table t_active_game
(
    id           serial primary key,
    id_game      int    not null references t_game (id),
    id_user      int    not null references t_user (id),
    created_date timestamp not null default now()
);