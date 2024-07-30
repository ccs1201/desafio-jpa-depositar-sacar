create table if not exists depositar_sacar.usuario_version
(
    id      integer generated by default as identity
        primary key,
    saldo   numeric(12, 2) not null,
    version smallint,
    nome    varchar(100)   not null
        unique
);