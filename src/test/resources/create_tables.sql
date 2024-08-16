create table if not exists managers
(
    id             UUID primary key,
    first_name     varchar(100) ,
    last_name      varchar(100) ,
    status         varchar(100) ,
    created_at     timestamp,
    updated_at     timestamp
);
create table if not exists products
(
    id             UUID primary key,
    name           varchar(100) not null,
    status         varchar(100) not null,
    currency_code  varchar(100) not null,
    interest_rate  decimal(3, 2),
    product_limit  decimal(16, 0),
    created_at     timestamp,
    updated_at     timestamp,
    manager_id     uuid,

    foreign key (manager_id) references managers (id)
);
create table if not exists clients
(
    id            UUID primary key,
    status        varchar(100) not null,
    tax_code      varchar(100) not null,
    first_name    varchar(100) not null,
    last_name     varchar(100) not null,
    email         varchar(100) not null,
    address       varchar(100) not null,
    phone_number  varchar(100) not null,
    created_at    timestamp,
    updated_at    timestamp,
    manager_id    uuid,
    foreign key (manager_id) references managers (id)
);

create table if not exists accounts
(
    id            UUID primary key,
    name          varchar(100) not null,
    type  varchar(100) not null,
    status        varchar(100) not null,
    currency_code varchar(100) not null,
    balance       decimal(15, 2),
    created_at    timestamp,
    updated_at    timestamp,
    client_id     uuid,
    foreign key (client_id) references clients (id)
);
create table if not exists agreements
(
    id            UUID primary key,
    interest_rate decimal(3, 2),
    status        varchar(100) not null,
    sum           decimal(16, 2),
    created_at    timestamp,
    updated_at    timestamp,
    product_id    uuid,
    account_id    uuid,
    foreign key (product_id) references products (id),
    foreign key (account_id) references accounts (id)
);
create table if not exists cards
(
    id                UUID primary key,
    card_number       varchar(100) not null,
    expiration_date   timestamp,
    created_at        timestamp,
    updated_at        timestamp,
    transaction_limit decimal(16, 2),
    card_type         varchar(100) not null,
    payment_system    varchar(100) not null,
    status            varchar(100) not null,
    account_id        uuid,
    foreign key (account_id) references accounts (id)
);
create table if not exists transactions
(
    id                UUID primary key,
    transaction_type  varchar(100) not null,
    amount            decimal(16, 2),
    description       varchar(100) not null,
    created_at        timestamp,
    debit_account_id  uuid,
    credit_account_id uuid,
    foreign key (debit_account_id) references accounts (id),
    foreign key (credit_account_id) references accounts (id)
);
