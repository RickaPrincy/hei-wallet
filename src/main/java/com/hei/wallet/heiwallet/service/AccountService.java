package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAll(){
        try {
            return accountRepository.findAll();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public List<Account> saveOrUpdateAll(List<Account> accounts) {
        try {
            return accountRepository.saveOrUpdateAll(accounts);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}