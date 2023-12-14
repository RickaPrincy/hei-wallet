CREATE OR REPLACE FUNCTION calculate_sum(
    "account_id" VARCHAR(255),
    "start_date" TIMESTAMP,
    "end_date" TIMESTAMP
) RETURNS DECIMAL(18,5) AS $$
DECLARE
    sum DECIMAL(18,5);
BEGIN
    SELECT COALESCE(SUM(CASE WHEN type = 'income' THEN amount ELSE -amount END), 0)
    INTO sum
    FROM "transaction"
    WHERE "account" = account_id
        AND transaction_datetime BETWEEN start_date AND end_date;

    RETURN sum;
END $$;

