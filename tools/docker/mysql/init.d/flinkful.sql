create database if not exists flinkful default character set utf8mb4 collate utf8mb4_unicode_ci;
use flinkful;

drop table if exists `flinkful_catalog_store`;
create table `flinkful_catalog_store`
(
    id            bigint       not null auto_increment,
    `type`        varchar(8)   not null,
    `name`        varchar(256) not null,
    configuration text,
    creator       varchar(32),
    create_time   datetime     not null default current_timestamp,
    editor        varchar(32),
    update_time   datetime     not null default current_timestamp on update current_timestamp,
    primary key (id),
    unique key uniq_catalog (`type`, `name`)
) engine = innodb comment ='catalog store';

drop table if exists `flinkful_catalog_database`;
create table `flinkful_catalog_database`
(
    id          bigint       not null auto_increment,
    `type`      varchar(8)   not null,
    `catalog`   varchar(256) not null,
    `name`      varchar(256) not null,
    properties  text,
    remark      varchar(256),
    creator     varchar(32),
    create_time datetime     not null default current_timestamp,
    editor      varchar(32),
    update_time datetime     not null default current_timestamp on update current_timestamp,
    primary key (id),
    unique key uniq_name (`type`, `catalog`, `name`)
) engine = innodb comment ='catalog database';

drop table if exists `flinkful_catalog_table`;
create table `flinkful_catalog_table`
(
    id             bigint       not null auto_increment,
    database_id    bigint       not null,
    kind           varchar(32)  not null,
    `name`         varchar(256) not null,
    properties     text,
    `schema`       text,
    original_query text,
    expanded_query text,
    remark         varchar(256),
    creator        varchar(32),
    create_time    datetime     not null default current_timestamp,
    editor         varchar(32),
    update_time    datetime     not null default current_timestamp on update current_timestamp,
    primary key (id),
    unique key uniq_name (database_id, kind, `name`)
) engine = innodb comment ='catalog table';

drop table if exists flinkful_catalog_function;
create table `flinkful_catalog_function`
(
    id                bigint       not null auto_increment,
    database_id       bigint       not null,
    `name`            varchar(256) not null,
    class_name        varchar(256) not null,
    function_language varchar(8)   not null,
    remark            varchar(256),
    creator           varchar(32),
    create_time       datetime     not null default current_timestamp,
    editor            varchar(32),
    update_time       datetime     not null default current_timestamp on update current_timestamp,
    primary key (id),
    unique key uniq_name (database_id, `name`)
) engine = innodb comment ='catalog function';
