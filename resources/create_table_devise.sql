CREATE TABLE IF NOT EXISTS "currency" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(255) NOT NULL,
    "code" VARCHAR(50) NOT NULL,
    "symbol" VARCHAR(10) UNIQUE,
    "exchange_rate" FLOAT NOT NULL,
    "updated_at" DATE DEFAULT CURRENT_DATE NOT NULL
);

INSERT INTO "currency"
SELECT 'currency_dollar', 'Dollar US', 'USD', '$', 1.0, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "currency"
    WHERE
        "id"='currency_dollar' AND "name"='Dollar US' AND "code"='USD'
        AND "symbol"='$' AND "exchange_rate"=1.0 AND "updated_at"='2023-01-01'
);

INSERT INTO "currency"
SELECT 'currency_euro', 'Euro', 'EUR', '€', 0.85, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "currency"
    WHERE
        "id"='currency_euro' AND "name"='Euro' AND "code"='EUR'
        AND "symbol"='€' AND "exchange_rate"=0.85 AND "updated_at"='2023-01-01'
);

INSERT INTO "currency"
SELECT 'currency_ariary', 'Ariary', 'MGA', 'Ar', 0.0002, '2023-01-01'
WHERE NOT EXISTS (
    SELECT 1 FROM "currency"
    WHERE
            "id"='currency_ariary' AND "name"='Ariary' AND "code"='MGA'
      AND "symbol"='Ar' AND "exchange_rate"=0.0002 AND "updated_at"='2023-01-01'
);
