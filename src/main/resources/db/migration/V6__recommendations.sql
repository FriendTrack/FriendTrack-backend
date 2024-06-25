create table recommendations
(
    id          uuid default uuid_generate_v4() primary key,
    field_type  text check (field_type in ('EMPATHY', 'TRUST', 'COMMUNICATION', 'RESPECT', 'TIME')) not null,
    title       varchar(255)                                                                        not null,
    description text                                                                                not null
);


-- COMMUNICATION
INSERT INTO recommendations (field_type, title, description)
VALUES ('COMMUNICATION', 'Не отвлекайтесь',
        'Отвлекаться на телефон во время диалога — отличный способ испортить взаимоотношения с окружающими. Если в этом цель, тогда отлично, но если вы хотите прослыть хорошим собеседником, забудьте о том, что он у вас есть. Мешает не только телефон: это может быть все, что угодно, что мешает разговору. Например, какая-нибудь вещь, стоящая между вами, шум, некомфортная обстановка.'),
       ('COMMUNICATION', 'Задавайте вопросы',
        'Многие отказываются задавать вопросы, боясь показаться навязчивыми или глупыми. На самом же деле это могущественный инструмент, который позволит заинтересовать собеседника и показать ему, что он вам интересен. Повторяйте последние слова в вопросительной форме, что будет поощрять его рассказывать дальше. Те, кого называют общительными людьми, чаще всего умеют всего лишь задавать вопросы и внимательно слушать.'),
       ('COMMUNICATION', 'Избавьтесь от всего лишнего',
        'Под лишним нужно понимать «эм» и «ам», а также слова и словосочетания, не несущие смысла. Сократите количество подобного мусора, чтобы казаться более уверенным и убедительным. Сначала это покажется трудной задачей, но с практикой вы не только избавитесь от лишних слов, но и станете яснее мыслить.');

-- TRUST
INSERT INTO recommendations (field_type, title, description)
VALUES ('TRUST', 'Сдерживайте свои обещания',
        'Для того чтобы построить доверие, необходимо не только выполнять обещания, но и избегать давать те, которые вы не в состоянии выполнить. Соблюдение данного слова повышает ваш авторитет, делает вас более уважаемым в глазах окружающих, которые начнут серьезнее относиться к вам и ваши слова будут восприниматься с большим вниманием.'),
       ('TRUST', 'Помните, что для завоевания доверия нужно время',
        'Не стоит ждать мгновенных результатов. Начинайте с небольших шагов для укрепления доверия, и со временем вы сможете оценить, насколько целесообразно брать на себя большую ответственность. Важно также научиться доверять другим, проверяя тем самым их истинные намерения.'),
       ('TRUST', 'Обдумывайте свои решения',
        'Соглашайтесь только на то, с чем вы уверены, что справитесь. Избегайте брать на себя слишком много обязательств одновременно. Не стесняйтесь говорить «нет», когда чувствуете, что предложение вас не заинтересовало. Невыполнение обещаний приводит к разочарованиям, и их количество будет только расти.'),
       ('TRUST', 'Будьте честны',
        'Ложь, как правило, используется для достижения какой-либо выгоды: защиты кого-то, избегания проблем, введения в заблуждение и т.д. Чтобы заслужить доверие, важно говорить правду. Подумайте о последствиях, когда вашу ложь раскроют – доверие к вам немедленно утратится.');

-- TIME
INSERT INTO recommendations (field_type, title, description)
VALUES ('TIME', 'Планируйте встречи',
        'Организуйте встречи заранее, чтобы оба могли заранее спланировать свое время и быть свободными для общения.'),
       ('TIME', 'Проводите время вместе',
        'Предлагайте провести время вместе, делая что-то приятное и интересное для обоих.'),
       ('TIME', 'Выясняйте интересы',
        'Узнайте, что интересует вашего собеседника, и попробуйте вовлечься в его увлечения.');

-- RESPECT
INSERT INTO recommendations (field_type, title, description)
VALUES ('RESPECT', 'Будьте внимательными и дружелюбными',
        'Проявляйте интерес к другому человеку, слушайте его, общайтесь с уважением и дружелюбием.'),
       ('RESPECT', 'Соблюдайте границы и уважайте личное пространство',
        'Уважение к чужой личной жизни и приватности является важным аспектом уважения к другому человеку.'),
       ('RESPECT', 'Выражайте благодарность',
        'Покажите признательность за помощь, поддержку и любой другой вид доброты, который вам оказывают.'),
       ('RESPECT', 'Следуйте принципу "держите свое слово"',
        'Будьте надежными и ответственными, выполняйте свои обещания и будьте человеком своего слова.');

-- EMPATHY
INSERT INTO recommendations (field_type, title, description)
VALUES ('EMPATHY', 'Слушайте активно',
        'Уделите время и внимание другому человеку, слушайте его без прерываний и судебных замечаний. Покажите, что вас интересуют его чувства и мысли.'),
       ('EMPATHY', 'Ставьте себя на его место',
        'Попробуйте почувствовать ситуацию глазами другого человека, ставьтесь на его место и пытайтесь понять его точку зрения.'),
       ('EMPATHY', 'Задавайте вопросы',
        'Задавайте вопросы, чтобы углубить понимание ситуации и чувств другого человека. Это поможет вам лучше узнать его и проявить интерес к его переживаниям.'),
       ('EMPATHY', 'Поддерживайте чувства без осуждения',
        'Признавайте чувства другого человека без осуждения или критики. Принимайте его эмоции как они есть, показывая, что вы понимаете его состояние.');
