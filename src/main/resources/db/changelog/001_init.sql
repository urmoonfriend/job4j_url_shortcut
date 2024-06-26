create table sites
(
    id       bigserial primary key not null,
    username varchar(2000) unique,
    password varchar(2000),
    site     varchar(2000),
    role     varchar(500)
);

create table urls
(
    id       uuid  primary key not null,
    url      text unique,
    site_id  bigserial,
    created_at timestamp without time zone
);

create table statistic
(
    id  bigserial primary key not null,
    url_id uuid unique not null,
    count int default 0
)

