CREATE TABLE IF NOT EXISTS "transaction" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "transaction_datetime" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "description" TEXT NOT NULL,
    "amount" DECIMAL(18,5) NOT NULL CHECK("amount" > 0),
    "source_account" VARCHAR(255) REFERENCES "account"("id") NOT NULL,
    "destination_account" VARCHAR(255) REFERENCES "account"("id") NOT NULL
);


INSERT INTO "transaction"
SELECT 'transaction_id1', '2023-01-01', 'transaction_description1', 100, 'account_id1','account_id2'
WHERE NOT EXISTS (
    SELECT 1 FROM "transaction"
    WHERE
            "id"='transaction_id1' AND "transaction_datetime"='2023-01-01' AND
            "description"='transaction_description1' AND "amount"=100 AND
            "source_account"='account_id1' AND "destination_account"='account_id2'
);

INSERT INTO "transaction"
SELECT 'transaction_id2', '2023-01-01', 'transaction_description2', 100, 'account_id2','account_id3'
WHERE NOT EXISTS (
    SELECT 1 FROM "transaction"
    WHERE
            "id"='transaction_id2' AND "transaction_datetime"='2023-01-01' AND
            "description"='transaction_description2' AND "amount"=100 AND
            "source_account"='account_id2' AND "destination_account"='account_id3'
);

INSERT INTO "transaction"
SELECT 'transaction_id3', '2023-01-01', 'transaction_description3', 100, 'account_id3','account_id1'
WHERE NOT EXISTS (
    SELECT 1 FROM "transaction"
    WHERE
            "id"='transaction_id3' AND "transaction_datetime"='2023-01-01' AND
            "description"='transaction_description3' AND "amount"=100 AND
            "source_account"='account_id3' AND "destination_account"='account_id1'
);
