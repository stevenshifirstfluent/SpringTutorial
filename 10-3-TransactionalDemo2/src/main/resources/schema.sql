drop table if exists account;

create table account (
  id bigint primary key,
  owner varchar(100),
  balance decimal(15,2)
);
