create table forms
(
    id uuid primary key,
    user_id uuid not null,
    interaction_count int not null default 0,
    date timestamp not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp not null,
    foreign key (user_id) references users(id) on delete cascade
);

create table contact_interactions
(
    form_id uuid not null,
    contact_id uuid not null,
    communication int default 0 check ((communication >= 0 and communication <= 5) or communication is null),
    respect int default 0 check ((respect >= 0 and respect <= 5) or respect is null),
    time int default 0 check ((time >= 0 and time <= 5) or time is null),
    trust int default 0 check ((trust >= 0 and trust <= 5) or trust is null),
    empathy int default 0 check ((empathy >= 0 and empathy <= 5) or empathy is null),
    foreign key (form_id) references forms (id) on delete cascade,
    foreign key (contact_id) references contacts (id) on delete cascade,
    primary key (form_id, contact_id)
);


