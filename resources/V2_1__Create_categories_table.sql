CREATE TABLE IF NOT EXISTS "category" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(225) UNIQUE NOT NULL,
    "type" VARCHAR(225) NOT NULL 
);

INSERT INTO "category"
SELECT 'category_1', 'Restaurant'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id"='category_1' AND "name"='Restaurant'
);
INSERT INTO "category"
SELECT 'category_2', 'Téléphone'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
            "id"='category_2' AND "name"='Téléphone'
);

INSERT INTO "category"
SELECT 'category_3', 'Multimedia'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
            "id"='category_3' AND "name"='Multimedia'
);