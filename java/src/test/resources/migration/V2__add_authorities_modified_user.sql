create table authorities
(
    username  varchar(50) not null references users (username),
    authority varchar(50) not null
);

create unique index ix_auth_username on authorities (username, authority);

alter table users
    add `enabled` boolean not null default '1'
