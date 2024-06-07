create table forms
(
    id uuid primary key,
    user_id uuid not null,
    mood varchar(100) not null default 'NEUTRAL',
    interaction_count int not null default 0,
    date timestamp not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp not null,
    foreign key (user_id) references users(id)
);

create table contact_interactions
(
    form_id uuid not null,
    contact_id uuid not null,
    emotion varchar(100) not null default 'LIKE',
    foreign key (form_id) references forms (id),
    foreign key (contact_id) references contacts (id),
    primary key (form_id, contact_id)
);