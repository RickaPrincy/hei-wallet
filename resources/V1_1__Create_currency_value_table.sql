CREATE TABLE IF NOT EXISTS "currency_value" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "source" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "destination" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "amount" DECIMAL(18,5) DEFAULT 0 NOT NULL,
    "effective_date" DATE DEFAULT CURRENT_DATE
);
INSERT INTO "currency_value"
SELECT 'history_entry_2', 2000.00000, '2023-12-08T12:00:00Z', 'account_id2'
WHERE NOT EXISTS (
    SELECT 1 FROM "balance_history"
    WHERE
            "id"='history_entry_2' AND "balance"=2000.00000 AND "creation_datetime"='2023-12-08T12:00:00Z' AND "account"='account_id2'
);

INSERT INTO "balance_history"
SELECT 'history_entry_3', 2005.00000, '2023-12-08T12:00:00Z', 'account_id3'
WHERE NOT EXISTS (
    SELECT 1 FROM "balance_history"
    WHERE
            "id"='history_entry_3' AND "balance"=2005.00000 AND "creation_datetime"='2023-12-08T12:00:00Z' AND "account"='account_id3'
);
