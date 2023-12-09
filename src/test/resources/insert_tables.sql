-- Insert data into the managers table
INSERT INTO managers (id, first_name, last_name, manager_status)
VALUES ('6e42ea78-969c-11ee-b9d1-0242ac120002', 'John', 'Doe', 'ACTIVE'),
       ('7e964ba4-969c-11ee-b9d1-0242ac120002', 'Jane', 'Smith', 'INACTIVE');

-- Insert data into the products table
INSERT INTO products (id, name, product_status, currency_code, interest_rate, product_limit,
                      manager_id)
VALUES ('8df40ce4-969c-11ee-b9d1-0242ac120002', 'Savings Account', 'ACTIVE', 'USD', 0.02, 10000,
        '6e42ea78-969c-11ee-b9d1-0242ac120002'),
       ('a96788e8-969c-11ee-b9d1-0242ac120002', 'Credit Card', 'ACTIVE', 'USD', 0.15, 5000,
        '6e42ea78-969c-11ee-b9d1-0242ac120002');

-- Insert data into the clients table
INSERT INTO clients (id, client_status, tax_code, first_name, last_name, email, address, phone_number, manager_id)
VALUES ('b3a3a896-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '123456789', 'Alice', 'Johnson', 'alice@gmail.com',
        '123 Main St', '123-456-7890', '6e42ea78-969c-11ee-b9d1-0242ac120002'),
       ('b9814aa2-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '987654321', 'Bob', 'Smith', 'bob@gmail.com', '456 Oak St',
        '456-789-0123', '6e42ea78-969c-11ee-b9d1-0242ac120002');

-- Insert data into the accounts table
INSERT INTO accounts (id, name, account_type, status, currency_code, balance, created_at, updated_at, client_id)
VALUES
    ('d7d5866c-969c-11ee-b9d1-0242ac120002', 'Alice', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b3a3a896-969c-11ee-b9d1-0242ac120002'),
    ('06c8dc62-969d-11ee-b9d1-0242ac120002', 'Bob', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b9814aa2-969c-11ee-b9d1-0242ac120002');


-- Insert data into the agreements table
INSERT INTO agreements (id, interest_rate, status, sum, product_id, account_id)
VALUES ('4e1f1090-969d-11ee-b9d1-0242ac120002', 0.02, 'ACTIVE', 1000,
        '8df40ce4-969c-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
       ('546651ac-969d-11ee-b9d1-0242ac120002', 0.15, 'ACTIVE', 500,
        'a96788e8-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002');

-- Insert data into the cards table
INSERT INTO cards (id, card_number, expiration_date, transaction_limit, card_type, payment_system, status, account_id)
VALUES
    ('7f580996-969d-11ee-b9d1-0242ac120002', '1234-5678-9012-3456', '2023-12-31', 1000, 'CREDIT', 'VISA', 'ACTIVE', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
    ('92f1e468-969d-11ee-b9d1-0242ac120002', '9876-5432-1098-7654', '2023-12-31', 500, 'DEBIT', 'MASTER_CARD', 'ACTIVE', '06c8dc62-969d-11ee-b9d1-0242ac120002');

-- Insert data into the transactions table
INSERT INTO transactions (id, transaction_type, amount, description, debit_account_id, credit_account_id)
VALUES ('a80e7834-969d-11ee-b9d1-0242ac120002', 'DEPOSIT', 1000, 'Initial deposit',
        'd7d5866c-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002'),
       ('ad05644c-969d-11ee-b9d1-0242ac120002', 'PURCHASE', 50, 'Groceries',
        '06c8dc62-969d-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
       ('b21e6b2c-969d-11ee-b9d1-0242ac120002', 'TRANSFER', 200, 'Transfer to Bob',
        'd7d5866c-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002');
