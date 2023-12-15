# :money_with_wings: :moneybag: :dollar: Hei wallet

### Getting Started
 
- Set up environment variables as follows

```shell
DB_PASSWORD=123456 #your password 
DB_URL=jdbc:postgresql://localhost:5432/hei_wallet
DB_USERNAME=prog_admin #your postgresql username
```

- Create the database using the [migration files](resources/). This directory contains SQL files following the naming convention:
    - [create_database_hei_wallet.sql](resources/V0_1__Create_database_hei_wallet.sql)
    - [create_role_prog_admin.sql](resources/V0_2__Create_role_prog_admin.sql)
    - [create_extension_uuid.sql](resources/V0_3__Create_extension_uuid.sql)
    - [create_table_currency.sql](resources/V0_4__Create_table_currency.sql)
    - [create_account_type.sql](resources/V0_5__Create_account_type.sql)
    - [create_table_account.sql](resources/V0_6__Create_table_account.sql)
    - [create_transaction_type.sql](resources/V0_7__Create_transaction_type.sql)
    - [create_table_transaction.sql](resources/V0_8__Create_table_transaction.sql)

### Openapi

Click [here](https://petstore.swagger.io?url=https://raw.githubusercontent.com/RickaPrincy/hei-wallet/main/docs/api.yml) to watch the docs on petstore