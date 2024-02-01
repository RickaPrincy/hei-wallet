CREATE TABLE IF NOT EXISTS "currency_value" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "source" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "destination" VARCHAR(255) REFERENCES "currency"("id") NOT NULL,
    "amount" DECIMAL(18,5) DEFAULT 0 NOT NULL,
    "effective_date" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO "currency_value"
SELECT 'currency_value_1', 'currency_euro', 'currency_ariary', 2000, '2023-12-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "currency_value"
    WHERE
        "id"='currency_value_1' AND "source"='currency_euro' AND "destination"='curency_ariary' AND "amount"=2000
        AND "effective_date" = '2023-12-01'
);

INSERT INTO "currency_value"
SELECT 'currency_value_2', 'currency_ariary', 'currency_euro', 1, '2023-12-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "currency_value"
    WHERE
            "id"='currency_value_2' AND "destination"='currency_euro' AND "source"='curency_ariary' AND "amount"=1000
      AND "effective_date" = '2023-12-01'
);
