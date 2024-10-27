create table if not exists users
(
    id        binary(16) primary key,
    user_name varchar(255) not null unique,
    password  varchar(255) not null,
    email     varchar(255) not null unique
);

create table if not exists budgets
(
    id               bigint auto_increment primary key,
    name             varchar(255) not null,
    total_amount     binary(16)   not null,
    remaining_amount binary(16),
    start_date       date         not null,
    end_date         date         not null,
    user_id          binary(16),
    constraint fk_user
        foreign key (user_id)
            references users (id)
);

create table if not exists expenses
(
    id          bigint auto_increment primary key,
    description varchar(255),
    amount      binary(16),
    date        date,
    category    varchar(255),
    budget_id   bigint,
    constraint fk_budget_expense
        foreign key (budget_id)
            references budgets (id)
);

create table if not exists incomes
(
    id        bigint auto_increment primary key,
    source    varchar(255),
    amount    binary(16),
    date      date,
    category  varchar(255),
    budget_id bigint,
    constraint fk_budget_income
        foreign key (budget_id)
            references budgets (id)
);