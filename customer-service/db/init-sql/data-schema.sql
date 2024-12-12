CREATE TABLE customers (
                           customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           phone_number VARCHAR(50),
                           tax_number VARCHAR(255) NOT NULL,
                           address TEXT,
                           city VARCHAR(255) NOT NULL,
                           zip_code VARCHAR(50) NOT NULL,
                           country VARCHAR(255) NOT NULL,
                           customer_status VARCHAR(50) NOT NULL CHECK (customer_status IN ('PENDING', 'ACTIVE', 'SUSPENDED')),
                           user_id VARCHAR(255),
                           created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT unique_email UNIQUE (email)
);
