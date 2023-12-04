DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transaction_type') THEN
        CREATE TYPE "transaction_type" AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');
    END IF;
END $$