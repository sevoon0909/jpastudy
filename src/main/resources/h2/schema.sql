DROP TABLE IF EXISTS membership CASCADE;
DROP TABLE IF EXISTS user_membership CASCADE;

create table membership (
    membership_id varchar(255) not null,
    membership_name varchar(255),
    primary key (membership_id)
);

create table user_membership (
    seq bigint not null,
    membership_status varchar(255),
    point integer,
    start_date timestamp,
    user_id varchar(255),
    membership_id varchar(255),
    primary key (seq)
);

alter table user_membership
   add constraint FKl6k3oqc2tjhb36w2qpnk7kao0
   foreign key (membership_id)
   references membership;
