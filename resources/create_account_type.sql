DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'account_type') THEN
        CREATE TYPE "account_type" AS ENUM ('CHECKING', 'SAVING', 'BUSINESS', 'INVESTMENT', 'CERTIFICATE_OF_DEPOSIT');
    END IF;
END $$;