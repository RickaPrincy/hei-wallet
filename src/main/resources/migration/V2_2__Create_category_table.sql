CREATE TABLE IF NOT EXISTS "category" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(225) UNIQUE NOT NULL,
    "type" "category_type" NOT NULL
);

INSERT INTO "category"
SELECT 'category_1', 'Restaurant', 'ALL'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id"='category_1' AND "name"='Restaurant'
);
INSERT INTO "category"
SELECT 'category_2', 'Téléphone', 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id"='category_2' AND "name"='Téléphone'
);

INSERT INTO "category"
SELECT 'category_3', 'Multimedia', 'CREDIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id"='category_3' AND "name"='Multimedia'
);

INSERT INTO "category"
SELECT 'category_transfer', 'TRANSFER', 'ALL'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id"='category_transfer' AND "name"='TRANSFER'
);
