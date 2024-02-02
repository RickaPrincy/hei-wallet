package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.*;
import com.hei.wallet.heiwallet.exception.NotFoundException;
import com.hei.wallet.heiwallet.service.BalanceService;
import com.hei.wallet.heiwallet.service.CurrencyService;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    private final CurrencyMapper currencyMapper;
    private final BalanceMapper balanceMapper;
    private final CurrencyService currencyService;
    private final BalanceService balanceService;

    public com.hei.wallet.heiwallet.model.Account createToDomain(CreateAccount account){
        final com.hei.wallet.heiwallet.model.Currency currency = currencyService.findById(account.getCurrencyId());
        if(currency == null){
            throw new NotFoundException("Destination or source currency are not found");
        }
        return new com.hei.wallet.heiwallet.model.Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currency,
                null
        );
    }

    public Account toRest(com.hei.wallet.heiwallet.model.Account account){
        com.hei.wallet.heiwallet.model.Balance balance = balanceService.findCurrentBalanceByAccountId(account.getId()).orElse(null);
        return new Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currencyMapper.toRest(account.getCurrency()),
                balanceMapper.toRest(balance)
        );
    }

    public AccountMapper(
            CurrencyMapper currencyMapper,
            BalanceMapper balanceMapper,
            CurrencyService currencyService,
            BalanceService balanceService
    ) {
        this.currencyMapper = currencyMapper;
        this.balanceMapper = balanceMapper;
        this.currencyService = currencyService;
        this.balanceService = balanceService;
    }
}