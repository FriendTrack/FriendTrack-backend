CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table questions (
    id uuid primary key default uuid_generate_v4(),
    question text not null,
    field_type text check (field_type in ('EMPATHY', 'TRUST', 'COMMUNICATION', 'RESPECT', 'TIME')) not null
);

create table user_answers (
    id uuid primary key,
    question_id uuid not null,
    user_id uuid not null,
    contact_id uuid not null,
    value int not null check (value >= 0 and value <= 5),
    created_at timestamp not null default now(),
    foreign key (question_id) references questions(id) on delete cascade,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (contact_id) references contacts(id) on delete cascade
);


