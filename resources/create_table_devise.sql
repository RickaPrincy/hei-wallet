CREATE TABLE IF NOT EXISTS "currency" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(255) NOT NULL,
    "code" VARCHAR(50) NOT NULL,
    "symbol" VARCHAR(10) UNIQUE,
    "exchange_rate" FLOAT NOT NULL,
    "udpated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);