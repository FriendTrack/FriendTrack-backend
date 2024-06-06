create table contacts (
    id uuid primary key,
    name varchar(100) not null,
    details varchar(200),
    link varchar(300),
    birth_date date,
    user_id uuid not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp not null,
    foreign key (user_id) references users(id)
);