create table dish
(
    id            bigint auto_increment,
    mass_in_grams int,
    description   varchar(100),
    proteins      float,
    fats          float,
    carbohydrates float,
    primary key (id)
);
