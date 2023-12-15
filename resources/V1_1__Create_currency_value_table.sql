CREATE TABLE IF NOT EXISTS "currency_value" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "source" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "destination" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "amount" DECIMAL(18,5) DEFAULT 0 NOT NULL,
    "effective_date" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
