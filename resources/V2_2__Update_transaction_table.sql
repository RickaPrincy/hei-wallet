ALTER TABLE "transaction"
ADD COLUMN IF NOT EXISTS "transaction_date" DATETIME NOT NULL,
ADD COLUMN IF NOT EXISTS "category" VARCHAR(255) DEFAULT 'category_1' NOT NULL,
ADD CONSTRAINT fk_transaction_category FOREIGN KEY ("category") REFERENCES "category"("id");

