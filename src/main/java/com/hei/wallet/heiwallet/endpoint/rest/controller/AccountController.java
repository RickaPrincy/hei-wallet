package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.AccountMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Account;
import com.hei.wallet.heiwallet.endpoint.rest.model.CreateAccount;
import com.hei.wallet.heiwallet.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/accounts")
    public List<Account> getAll(){
        return accountService
                .getAll()
                .stream()
                .map(accountMapper::toRest).toList();
    }

    @PutMapping("/accounts")
    public List<Account> crupdateAll(@RequestBody List<CreateAccount> createAccounts){
        return accountService.saveOrUpdateAll(
                createAccounts.stream().map(accountMapper::createToDomain).toList()
        ).stream().map(accountMapper::toRest).toList();
    }

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }
}
