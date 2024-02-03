package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Balance;
import com.hei.wallet.heiwallet.model.Transaction;
import com.hei.wallet.heiwallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findAllByAccountId(String accountId){
        try {
            return transactionRepository.findByField("account", accountId);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
