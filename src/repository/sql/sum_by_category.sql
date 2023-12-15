CREATE OR REPLACE FUNCTION sum_in_out_by_category(
    "account_id" VARCHAR(255),
    "from" TIMESTAMP,
    "to" TIMESTAMP
) RETURNS TABLE (category_name VARCHAR(255), total_amount DECIMAL(18,5)) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.name AS category_name,
        COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE -t.amount END), 0) AS total_amount
    FROM
        category c
    LEFT JOIN
        "transaction" t ON c.id = t.category AND t.account = account_id
              AND t.transaction_datetime BETWEEN "from" AND "to"
    GROUP BY
        c.name;
END; $$ LANGUAGE plpgsql;
