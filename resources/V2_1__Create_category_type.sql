DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'category_type') THEN
        CREATE TYPE "category_type" AS ENUM ( 'CREDIT', 'DEBIT' , 'ALL' );
    END IF;
END $$;