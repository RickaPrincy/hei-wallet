package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.AccountMapper;
import com.hei.wallet.heiwallet.endpoint.rest.mapper.TransactionMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Account;
import com.hei.wallet.heiwallet.endpoint.rest.model.CreateTransaction;
import com.hei.wallet.heiwallet.service.TransactionService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper, AccountMapper accountMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.accountMapper = accountMapper;
    }

    @PutMapping("/transactions")
    public Account doTransaction(@RequestBody CreateTransaction transaction){
        return accountMapper.toRest(
                transactionService.doTransaction(transactionMapper.createToDomain(transaction))
        );
    }
}
