CREATE TABLE IF NOT EXISTS "account" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(255) NOT NULL,
    "balance" DECIMAL(18,5) NOT NULL,
    "number" VARCHAR(50) UNIQUE NOT NULL,
    "creation_date" DATE DEFAULT CURRENT_DATE NOT NULL,
    "type" "account_type" NOT NULL,
    "comment" TEXT,
    -- TODO: make owner and bank_name foreign key to another table
    "owner" VARCHAR(255) NOT NULL,
    "bank_name" VARCHAR(255) NOT NULL,
    "currency" VARCHAR(255) REFERENCES "currency"("id") NOT NULL
);

INSERT INTO "account"
SELECT 'account_id1', 'account_name1', 2000, 'account_number1', '2023-02-02', 'SAVING', 'account_comment1','account_owner1','account_bank1','currency_dollar'
WHERE NOT EXISTS (
    SELECT 1 FROM "account"
    WHERE
        "id"='account_id1' AND "name"='account_name1' AND "balance"=2000 AND
        "number"='account_number1' AND "creation_date"='2023-02-02' AND
        "type"='SAVING' AND "comment"='account_comment1' AND "owner"='account_owner1' AND
        "bank_name"='account_bank1' AND "currency"='currency_dollar'
);

INSERT INTO "account"
SELECT 'account_id2', 'account_name2', 1000, 'account_number2', '2023-03-03', 'CHECKING', 'account_comment2','account_owner2','account_bank2','currency_euro'
WHERE NOT EXISTS (
    SELECT 1 FROM "account"
    WHERE
        "id"='account_id2' AND "name"='account_name2' AND "balance"=1000 AND
        "number"='account_number2' AND "creation_date"='2023-03-03' AND
        "type"='CHECKING' AND "comment"='account_comment2' AND "owner"='account_owner2' AND
        "bank_name"='account_bank2' AND "currency"='currency_euro'
);

INSERT INTO "account"
SELECT 'account_id3', 'account_name3', 5000, 'account_number3', '2023-03-03', 'INVESTMENT', 'account_comment3','account_owner3','account_bank3','currency_ariary'
WHERE NOT EXISTS (
    SELECT 1 FROM "account"
    WHERE
        "id"='account_id3' AND "name"='account_name3' AND "balance"=5000 AND
        "number"='account_number3' AND "creation_date"='2023-03-03' AND
        "type"='INVESTMENT' AND "comment"='account_comment3' AND "owner"='account_owner3' AND
        "bank_name"='account_bank3' AND "currency"='currency_ariary'
);
