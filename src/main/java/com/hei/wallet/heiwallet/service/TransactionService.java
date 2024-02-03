package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.BadRequestException;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.*;
import com.hei.wallet.heiwallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, BalanceService balanceService, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.balanceService = balanceService;
        this.accountService = accountService;
    }

    public List<Transaction> findAllByAccountId(String accountId){
        try {
            return transactionRepository.findByField("account", accountId);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Account doTransaction(Transaction transaction){
        Account account = transaction.getAccount();
        Balance currentBalance = balanceService
                .findCurrentBalanceByAccountId(account.getId())
                .orElse(new Balance("", BigDecimal.ZERO, Instant.now(), account));
        boolean isBalanceEnough = currentBalance.getAmount().compareTo(transaction.getAmount()) >= 0;

        if(transaction.getType() == TransactionType.DEBIT && !isBalanceEnough){
            throw new BadRequestException("Balance not enough");
        }
        currentBalance.setId(UUID.randomUUID().toString());
        currentBalance.setCreationDatetime(transaction.getTransactionDatetime());
        BigDecimal newAmount;

        if(transaction.getType() == TransactionType.CREDIT){
            if(transaction.getCategory().getType() == CategoryType.CREDIT)
                throw new BadRequestException("Category not available for the transaction type");
            newAmount = currentBalance.getAmount().add(transaction.getAmount());
        }else{
            if(transaction.getCategory().getType() == CategoryType.DEBIT)
                throw new BadRequestException("Category not available for the transaction type");
            newAmount = currentBalance.getAmount().subtract(transaction.getAmount());
        }

        currentBalance.setAmount(newAmount);
        try {
            transactionRepository.saveOrUpdate(transaction);
            balanceService.crupdateAll(List.of(currentBalance));
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return accountService.findById(account.getId());
    }
}
