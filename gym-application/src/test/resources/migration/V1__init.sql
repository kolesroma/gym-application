create table trainees
(
    id            bigint auto_increment,
    address       varchar(255),
    date_of_birth date,
    user_id       bigint,
    primary key (id)
);

create table trainers
(
    id             bigint auto_increment,
    specialization varchar(255),
    user_id        bigint,
    primary key (id)
);

create table training_types
(
    id                 bigint auto_increment,
    training_type_name varchar(255),
    primary key (id)
);

create table trainings
(
    id                bigint auto_increment,
    training_date     date,
    training_duration integer,
    training_name     varchar(255),
    trainee_id        bigint,
    trainer_id        bigint,
    training_type_id  bigint,
    primary key (id)
);

create table users
(
    id         bigint auto_increment,
    first_name varchar(255),
    is_active  boolean,
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255),
    primary key (id)
);

alter table trainees
    add constraint UK_trainees_user_id unique (user_id);

alter table trainers
    add constraint UK_trainers_user_id unique (user_id);


alter table trainees
    add constraint FK_trainees_user_id foreign key (user_id) references users (id);

alter table trainers
    add constraint FK_trainers_user_id foreign key (user_id) references users (id);

alter table trainings
    add constraint FK_trainings_trainee_id foreign key (trainee_id) references trainees (id);

alter table trainings
    add constraint FK_trainings_trainer_id foreign key (trainer_id) references trainers (id);

alter table trainings
    add constraint FK_trainings_training_type_id foreign key (training_type_id) references training_types (id);