CREATE OR REPLACE FUNCTION sum_in_out(
    account_id VARCHAR(255),
    "from" TIMESTAMP,
    "to" TIMESTAMP
) RETURNS DECIMAL(18,5) AS $$
DECLARE
    result DECIMAL(18,5);
BEGIN
    SELECT COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END), 0)
    INTO result
    FROM "transaction"
    WHERE "account" = account_id
      AND transaction_datetime BETWEEN "from" AND "to";
    RETURN result;
END;
$$ LANGUAGE plpgsql;