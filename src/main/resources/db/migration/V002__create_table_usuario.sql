create table if not exists depositar_sacar.usuario
(
    id    integer generated by default as identity
        primary key,
    saldo numeric(12, 2) not null,
    nome  varchar(100)   not null
        unique
);
