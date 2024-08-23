-- // FIXME can be renamed to insert_values
-- Insert data into the managers table
INSERT INTO managers (id, first_name, last_name, status)
VALUES
    ('8d25ab36-969c-11ee-b9d1-0242ac120002', 'Alice', 'Johnson', 'ACTIVE'),
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
INSERT INTO products (id, name, status, currency_code, interest_rate, product_limit, manager_id)
VALUES
    ('8df40ce4-969c-11ee-b9d1-0242ac120002', 'Savings Account', 'ACTIVE', 'USD', 0.02, 10000, '8d25ab36-969c-11ee-b9d1-0242ac120002'),
    ('a96788e8-969c-11ee-b9d1-0242ac120002', 'Credit Card', 'ACTIVE', 'USD', 0.15, 5000, '8d25ab36-969c-11ee-b9d1-0242ac120002');

-- Insert data into the clients table
INSERT INTO clients (id, status, tax_code, first_name, last_name, email, address, phone_number, manager_id)
VALUES
    ('b3a3a896-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '1234567890', 'Alice', 'Johnson', 'alice@gmail.com', '123 Main St', '123-456-7890', '8d25ab36-969c-11ee-b9d1-0242ac120002'),
    ('b9814aa2-969c-11ee-b9d1-0242ac120002', 'ACTIVE', '9876543210', 'Bob', 'Smith', 'bob@gmail.com', '456 Oak St', '456-789-0123', '8d25ab36-969c-11ee-b9d1-0242ac120002');

-- Insert data into the accounts table
INSERT INTO accounts (id, name, type, status, currency_code, balance, created_at, updated_at, client_id)
VALUES
    ('d7d5866c-969c-11ee-b9d1-0242ac120002', 'Alice', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b3a3a896-969c-11ee-b9d1-0242ac120002'),
    ('06c8dc62-969d-11ee-b9d1-0242ac120002', 'Bob', 'CHECKING_ACCOUNT', 'ACTIVE', 'USD', 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b9814aa2-969c-11ee-b9d1-0242ac120002');

-- Insert data into the agreements table
INSERT INTO agreements (id, interest_rate, status, sum, product_id, account_id)
VALUES
    ('4e1f1090-969d-11ee-b9d1-0242ac120002', 0.02, 'ACTIVE', 1000, '8df40ce4-969c-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002'),
    ('546651ac-969d-11ee-b9d1-0242ac120002', 0.15, 'ACTIVE', 500, 'a96788e8-969c-11ee-b9d1-0242ac120002', '06c8dc62-969d-11ee-b9d1-0242ac120002');

-- Insert data into the cards table
INSERT INTO cards (id, number, expiration_date, holder, card_type, payment_system, status, account_id, client_id)
VALUES
    ('7f580996-969d-11ee-b9d1-0242ac120002', '1234-5678-9012-3456', '2023-12-31', 'Alice Johnson', 'CREDIT_CARDS', 'VISA', 'ACTIVE', 'd7d5866c-969c-11ee-b9d1-0242ac120002', 'b3a3a896-969c-11ee-b9d1-0242ac120002'),
    ('92f1e468-969d-11ee-b9d1-0242ac120002', '9876-5432-1098-7654', '2023-12-31', 'Bob Smith', 'DEBIT_CARDS', 'MASTER_CARD', 'ACTIVE', '06c8dc62-969d-11ee-b9d1-0242ac120002', 'b9814aa2-969c-11ee-b9d1-0242ac120002');

-- Insert data into the transactions table
INSERT INTO transactions (id, type, amount, description, debit_account_id, credit_account_id, currency_code)
VALUES
    ('a80e7834-969d-11ee-b9d1-0242ac120002', 'DEPOSIT', 1000, 'Initial deposit', '06c8dc62-969d-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002', 'USD'),
    ('ad05644c-969d-11ee-b9d1-0242ac120002', 'PURCHASE', 50, 'Groceries', '06c8dc62-969d-11ee-b9d1-0242ac120002', 'd7d5866c-969c-11ee-b9d1-0242ac120002', 'USD');
