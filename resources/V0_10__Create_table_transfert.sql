CREATE TABLE IF NOT EXISTS "transfer" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "source" VARCHAR(255) REFERENCES "account"("id") NOT NULL,
    "destination" VARCHAR(255) REFERENCES "account"("id") NOT NULL,
    "amount" DECIMAL(18,5) NOT NULL,
    "creation_datetime" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO "transfer"
SELECT 'transfer_id1', 'account_id1', 'account_id2', 20, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "transfer"
    WHERE
        "id"='transfer_id1' AND "source"='account_id1' AND "destination"='account_id2'
         AND "amount"=20 AND "creation_datetime"='2023-01-01'
);

INSERT INTO "transfer"
SELECT 'transfer_id2', 'account_id2', 'account_id3', 50, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "transfer"
    WHERE
            "id"='transfer_id2' AND "source"='account_id2' AND "destination"='account_id3'
      AND "amount"=50 AND "creation_datetime"='2023-01-01'
);

INSERT INTO "transfer"
SELECT 'transfer_id3', 'account_id1', 'account_id3', 50, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "transfer"
    WHERE
            "id"='transfer_id3' AND "source"='account_id1' AND "destination"='account_id3'
      AND "amount"=50 AND "creation_datetime"='2023-01-01'
);