
create table if not exists portals (
                                       id          int primary key,
                                       portal_name text    not null,
                                       url         text    not null,
                                       users_active int    not null,
                                       working     boolean not null
);

create table if not exists joblistings (
                                           id        int primary key,
                                           job_title text    not null,
                                           company   text    not null,
                                           sphere    text    not null,
                                           is_active boolean not null
);