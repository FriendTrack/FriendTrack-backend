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
    interaction_type varchar(100) not null default 'TEXT' check (interaction_type in ('TEXT', 'CALL', 'MEETING')),
    happiness int default 0 check ((happiness >= 0 and happiness <= 10) or happiness is null),
    sadness int default 0 check ((sadness >= 0 and sadness <= 10) or sadness is null),
    fear int default 0 check ((fear >= 0 and fear <= 10) or fear is null),
    disgust int default 0 check ((disgust >= 0 and disgust <= 10) or disgust is null),
    anger int default 0 check ((anger >= 0 and anger <= 10) or anger is null),
    surprise int default 0 check ((surprise >= 0 and surprise <= 10) or surprise is null),
    interaction_mark varchar(100) not null default 'LIKE' check (interaction_mark in ('LIKE', 'DISLIKE')),
    foreign key (form_id) references forms (id),
    foreign key (contact_id) references contacts (id),
    primary key (form_id, contact_id)
);