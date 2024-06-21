create table questions (
    id uuid primary key,
    question text not null,
    field_type text check (field_type in ('EMPATHY', 'TRUST', 'COMMUNICATION', 'RESPECT', 'TIME')) not null
);


create table question_answers (
    id uuid primary key,
    question_id uuid not null,
    answer text not null,
    is_positive boolean not null,
    foreign key (question_id) references questions(id) on delete cascade
);

create table user_answers (
    id uuid primary key,
    question_id uuid not null,
    user_id uuid not null,
    contact_id uuid not null,
    answer_id uuid not null,
    created_at timestamp not null default now(),
    foreign key (question_id) references questions(id) on delete cascade,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (contact_id) references contacts(id) on delete cascade,
    foreign key (answer_id) references question_answers(id) on delete cascade
);


