package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.*;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.service.BalanceService;
import com.hei.wallet.heiwallet.service.CurrencyService;
import com.hei.wallet.heiwallet.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapper {
    private final CurrencyMapper currencyMapper;
    private final BalanceMapper balanceMapper;
    private final TransactionMapper transactionMapper;
    private final CurrencyService currencyService;
    private final BalanceService balanceService;
    private final TransactionService transactionService;

    public com.hei.wallet.heiwallet.model.Account createToDomain(CreateAccount account){
        final com.hei.wallet.heiwallet.model.Currency currency = currencyService.findById(account.getCurrencyId());
        final List<com.hei.wallet.heiwallet.model.Balance> balances = balanceService.findAllByAccountId(account.getId(),null,null);
        final List<com.hei.wallet.heiwallet.model.Transaction> transactions = transactionService.findAllByAccountId(account.getId());

        if(currency == null){
            throw new InternalServerErrorException("Unknown currency");
        }

        return new com.hei.wallet.heiwallet.model.Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currency,
                balances,
                transactions
        );
    }

    public Account toRest(com.hei.wallet.heiwallet.model.Account account){
        final com.hei.wallet.heiwallet.model.Balance balance = balanceService.findCurrentBalanceByAccountId(account.getId()).orElse(null);
        return new Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currencyMapper.toRest(account.getCurrency()),
                balance == null ? null : balanceMapper.toRest(balance),
                account.getTransactions().stream().map(transactionMapper::toRest).toList()
        );
    }

    public com.hei.wallet.heiwallet.model.Account toDomain(Account account){
        final List<com.hei.wallet.heiwallet.model.Balance> balance = balanceService.findAllByAccountId(account.getId(),null,null);
        final com.hei.wallet.heiwallet.model.Currency currency = currencyService.findById(account.getCurrency().getId());
        final List<com.hei.wallet.heiwallet.model.Transaction> transactions = transactionService.findAllByAccountId(account.getId());

        return new com.hei.wallet.heiwallet.model.Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currency,
                balance,
                transactions
        );
    }

    public AccountMapper(
            CurrencyMapper currencyMapper,
            BalanceMapper balanceMapper, TransactionMapper transactionMapper,
            CurrencyService currencyService,
            BalanceService balanceService, TransactionService transactionService
    ) {
        this.currencyMapper = currencyMapper;
        this.balanceMapper = balanceMapper;
        this.transactionMapper = transactionMapper;
        this.currencyService = currencyService;
        this.balanceService = balanceService;
        this.transactionService = transactionService;
    }
}