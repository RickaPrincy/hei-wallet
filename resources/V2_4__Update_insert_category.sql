CREATE TABLE IF NOT EXISTS "category" (
    "id" VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(225) UNIQUE NOT NULL,
    "type" "category_type" VARCHAR(225) NOT NULL
);

INSERT INTO "category"
SELECT
    'category_4' ,'Food' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_4' AND "name" = 'Food' AND "type" = 'DEBIT'
);

INSERT INTO "category" ("id", "name", "type")
SELECT
    'category_5','Coffee','BEBIT' 
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_5' AND "name" = 'Coffee' AND "type" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_6' ,'Resto' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_6' AND "name" = 'Resto' AND "type_ category" = 'DEBIT'
);
--TRANSPORT
INSERT INTO "category"
SELECT
    'category_7' ,'Taxi' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_7' AND "name" = 'Taxi' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_8' ,'Public_transport' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_8' AND "name" = 'Public_transport' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_9' ,'business_trip' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_9' AND "name" = 'business_tri^p' AND "type_ category" = 'DEBIT'
);
--MULTIMEDIA
INSERT INTO "category"
SELECT
    'category_10' ,'internet' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_10' AND "name" = 'internet' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_11' ,'phone' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_11' AND "name" = 'phone' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_12' ,'post' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_12' AND "name" = 'post' AND "type_ category" = 'DEBIT'
);
--INVESTISSEMENTS
INSERT INTO "category"
SELECT
    'category_13' ,'saving' , 'CREDIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_13' AND "name" = 'saving' AND "type_ category" = 'CREDIT'
);
INSERT INTO "category"
SELECT
    'category_14' ,'investments' , 'ALL'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_14' AND "name" = 'investments' AND "type_ category" = 'ALL'
);
INSERT INTO "category"
SELECT
    'category_15' ,'property' , 'ALL'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_15' AND "name" = 'property' AND "type_ category" = 'ALL'
);
--FRAIS FINANCIERS
INSERT INTO "category"
SELECT
    'category_16' ,'fines' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_16' AND "name" = 'fines' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_17' ,'insurance' , 'ALL'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_17' AND "name" = 'insurance' AND "type_ category" = 'ALL'
);
INSERT INTO "category"
SELECT
    'category_18' ,'taxes' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_18' AND "name" = 'taxes' AND "type_ category" = 'DEBIT'
);
--INCOME
INSERT INTO "category"
SELECT
    'category_19' ,'salaries' , 'CREDIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_19' AND "name" = 'salaries' AND "type_ category" = 'CREDIT'
);
INSERT INTO "category"
SELECT
    'category_20' ,'Subsidy contributions' , 'DEBIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_20' AND "name" = 'Subsidy contributions' AND "type_ category" = 'DEBIT'
);
INSERT INTO "category"
SELECT
    'category_21' ,' discount vouchers' , 'CREDIT'
WHERE NOT EXISTS (
    SELECT 1 FROM "category"
    WHERE
        "id" = 'category_21' AND "name" = ' discount vouchers' AND "type_ category" = 'CREDIT'
);