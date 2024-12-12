CREATE SCHEMA IF NOT EXISTS auth_service;
CREATE TABLE auth_service.roles (
                                    id UUID PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE auth_service.users (
                                    id UUID PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    role_id UUID NOT NULL,
                                    status varchar(255) check (status in ('PENDING','ACTIVATED','NEW')),
                                    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                    update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES auth_service.roles (id) ON DELETE CASCADE
);


INSERT INTO auth_service.roles (id, name)
VALUES (gen_random_uuid(), 'ADMIN'),
       (gen_random_uuid(), 'MANAGER'),
       (gen_random_uuid(), 'CUSTOMER') ON CONFLICT (name) DO NOTHING;