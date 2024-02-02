package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Balance;
import com.hei.wallet.heiwallet.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Optional<Balance> findCurrentBalanceByAccountId(String accountId){
        try {
            return balanceRepository.findAllByAccountId(accountId).stream().findFirst();
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public List<Balance> findAllByAccountId(String accountId) {
        try {
            return balanceRepository.findAllByAccountId(accountId);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }
}
