CREATE OR REPLACE FUNCTION calculate_category_sums(
    "account_id" VARCHAR(255),
    "start_date" TIMESTAMP,
    "end_date" TIMESTAMP
) RETURNS TABLE (category_name VARCHAR(255), total_amount DECIMAL(18,5)) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.name AS category_name,
        COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE -t.amount END), 0) AS total_amount
    FROM
        category c
    LEFT JOIN
        "transaction" t ON c.id = t.category_id AND t.account = account_id
                          AND t.transaction_datetime BETWEEN start_date AND end_date
    GROUP BY
        c.name;
END $$;
