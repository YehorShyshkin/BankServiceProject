-- Insert data into the managers table
INSERT INTO managers (id, first_name, last_name, status)
VALUES ('8d25ab36-969c-11ee-b9d1-0242ac120002', 'Alice', 'Johnson', 'ACTIVE'),
       ('9f50d828-969c-11ee-b9d1-0242ac120002', 'Bob', 'Williams', 'INACTIVE'),
       ('ab75f61c-969c-11ee-b9d1-0242ac120002', 'Charlie', 'Brown', 'ACTIVE'),
       ('bc96e2f8-969c-11ee-b9d1-0242ac120002', 'David', 'Miller', 'INACTIVE'),
       ('cd92ea5c-969c-11ee-b9d1-0242ac120002', 'Emily', 'Davis', 'ACTIVE'),
       ('de34fa80-969c-11ee-b9d1-0242ac120002', 'Frank', 'Garcia', 'INACTIVE'),
       ('ef75ba1c-969c-11ee-b9d1-0242ac120002', 'Grace', 'Martinez', 'ACTIVE'),
       ('f0920ab0-969c-11ee-b9d1-0242ac120002', 'Henry', 'Rodriguez', 'INACTIVE'),
       ('f203cb3e-969c-11ee-b9d1-0242ac120002', 'Ivy', 'Lopez', 'ACTIVE'),
       ('f314cd1c-969c-11ee-b9d1-0242ac120002', 'Jack', 'Wilson', 'INACTIVE'),
       ('f425de4a-969c-11ee-b9d1-0242ac120002', 'Katherine', 'Taylor', 'ACTIVE'),
       ('f536ef78-969c-11ee-b9d1-0242ac120002', 'Liam', 'Anderson', 'INACTIVE'),
       ('f647f8b6-969c-11ee-b9d1-0242ac120002', 'Mia', 'Thomas', 'ACTIVE'),
       ('f758a9d4-969c-11ee-b9d1-0242ac120002', 'Noah', 'Jackson', 'INACTIVE'),
       ('f869b0e2-969c-11ee-b9d1-0242ac120002', 'Olivia', 'White', 'ACTIVE'),
       ('f97a9d0a-969c-11ee-b9d1-0242ac120002', 'Paul', 'Harris', 'INACTIVE');

-- Insert data into the products table
INSERT INTO products (id, name, product_status, currency_code, interest_rate, product_limit, manager_id)
VALUES ('8df40ce4-969c-11ee-b9d1-0242ac120002', 'Savings Account', 'ACTIVE', 'USD', 0.02, 10000, '8d25ab36-969c-11ee-b9d1-0242ac120002'),
       ('a96788e8-969c-11ee-b9d1-0242ac120002', 'Credit Card', 'ACTIVE', 'USD', 0.15, 5000, '8d25ab36-969c-11ee-b9d1-0242ac120002'),
       ('b1b2a3c4-969c-11ee-b9d1-0242ac120002', 'Personal Loan', 'ACTIVE', 'USD', 0.07, 20000, '9f50d828-969c-11ee-b9d1-0242ac120002'),
       ('c3c4d5e6-969c-11ee-b9d1-0242ac120002', 'Mortgage', 'ACTIVE', 'USD', 0.03, 100000, 'ab75f61c-969c-11ee-b9d1-0242ac120002');

-- Insert data into the clients table
INSERT INTO clients (id, status, tax_code, first_name, last_name, email, address, phone_number, manager_id)
VALUES
    ('b3a3a896-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '1234567890', 'Alice', 'Johnson', 'alice@gmail.com', '123 Main St', '123-456-7890', '8d25ab36-969c-11ee-b9d1-0242ac120002'),
    ('b9814aa2-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '9876543210', 'Bob', 'Smith', 'bob@gmail.com', '456 Oak St', '456-789-0123', '8d25ab36-969c-11ee-b9d1-0242ac120002'),
    ('c0a1b2c3-969c-11ee-b9d1-0242ac120002', 'INACTIVE', '1122334455', 'Charlie', 'Brown', 'charlie@gmail.com', '789 Pine St', '789-012-3456', '9f50d828-969c-11ee-b9d1-0242ac120002'),
    ('d0b2c3d4-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '5566778890', 'Dana', 'Lee', 'dana@gmail.com', '321 Maple St', '321-654-9870', 'ab75f61c-969c-11ee-b9d1-0242ac120002');


-- Insert data into the accounts table
INSERT INTO accounts (id, name, account_type, status, currency_code, balance, created_at, updated_at, client_id)
VALUES
    ('d7d5866c-969c-11ee-b9d1-0242ac120002', 'Alice', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b3a3a896-969c-11ee-b9d1-0242ac120002'),
    ('06c8dc62-969d-11ee-b9d1-0242ac120002', 'Bob', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b9814aa2-969c-11ee-b9d1-0242ac120002'),
    ('17e9f80e-969d-11ee-b9d1-0242ac120002', 'Charlie', 'SAVINGS_ACCOUNT', 'ACTIVE', 'USD', 10000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'c0a1b2c3-969c-11ee-b9d1-0242ac120002'),
    ('28f0d6c8-969d-11ee-b9d1-0242ac120002', 'Dana', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 1500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'd0b2c3d4-969c-11ee-b9d1-0242ac120002');

-- Insert data into the agreements table
INSERT INTO agreements (id, interest_rate, status, sum, product_id, account_id)
VALUES
    ('4e1f1090-969d-11ee-b9d1-0242ac120002', 0.02, 'ACTIVE', 1000, '8df40ce4-969c-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
    ('546651ac-969d-11ee-b9d1-0242ac120002', 0.15, 'ACTIVE', 500, 'a96788e8-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002'),
    ('65778b2e-969d-11ee-b9d1-0242ac120002', 0.07, 'ACTIVE', 3000, 'b1b2a3c4-969c-11ee-b9d1-0242ac120002', '17e9f80e-969d-11ee-b9d1-0242ac120002'),
    ('76889c3d-969d-11ee-b9d1-0242ac120002', 0.03, 'ACTIVE', 15000, 'c3c4d5e6-969c-11ee-b9d1-0242ac120002', '28f0d6c8-969d-11ee-b9d1-0242ac120002');

-- Insert data into the cards table
INSERT INTO cards (id, card_number, expiration_date, transaction_limit, card_type, payment_system, status, account_id)
VALUES
    ('7f580996-969d-11ee-b9d1-0242ac120002', '1234-5678-9012-3456', '2023-12-31', 1000, 'CREDIT', 'VISA', 'ACTIVE', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
    ('92f1e468-969d-11ee-b9d1-0242ac120002', '9876-5432-1098-7654', '2023-12-31', 500, 'DEBIT', 'MASTER_CARD', 'ACTIVE', '06c8dc62-969d-11ee-b9d1-0242ac120002'),
    ('a0b1c2d3-969d-11ee-b9d1-0242ac120002', '1111-2222-3333-4444', '2024-06-30', 2000, 'CREDIT', 'AMEX', 'ACTIVE', '17e9f80e-969d-11ee-b9d1-0242ac120002'),
    ('b1c2d3e4-969d-11ee-b9d1-0242ac120002', '5555-6666-7777-8888', '2024-06-30', 1000, 'DEBIT', 'DISCOVER', 'ACTIVE', '28f0d6c8-969d-11ee-b9d1-0242ac120002');

-- Insert data into the transactions table
INSERT INTO transactions (id, transaction_type, amount, description, debit_account_id, credit_account_id)
VALUES
    ('a80e7834-969d-11ee-b9d1-0242ac120002', 'DEPOSIT', 1000, 'Initial deposit', 'd7d5866c-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002'),
    ('ad05644c-969d-11ee-b9d1-0242ac120002', 'PURCHASE', 50, 'Groceries', '06c8dc62-969d-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
    ('b21e6b2c-969d-11ee-b9d1-0242ac120002', 'TRANSFER', 200, 'Transfer to Bob', 'd7d5866c-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002'),
    ('c32d4e5f-969d-11ee-b9d1-0242ac120002', 'WITHDRAWAL', 300, 'ATM Withdrawal', '17e9f80e-969d-11ee-b9d1-0242ac120002', NULL),
    ('d43e5f6a-969d-11ee-b9d1-0242ac120002', 'TRANSFER', 500, 'Payment for loan', '28f0d6c8-969d-11ee-b9d1-0242ac120002', '17e9f80e-969d-11ee-b9d1-0242ac120002');
