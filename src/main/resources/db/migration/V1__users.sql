create table users(
      id uuid primary key,
      login text not null,
      email text not null,
      password text not null,
      username text,
      created_at timestamp not null,
      updated_at timestamp not null
);