package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.BadRequestException;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Balance;
import com.hei.wallet.heiwallet.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
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

    public List<Balance> findAllByAccountId(String accountId, Instant from, Instant to) {
        try {
            if(from == null || to == null){
                return balanceRepository.findAllByAccountId(accountId);
            }
            if(from.isAfter(to))
                throw new BadRequestException("from must be before to");
            if(to.isAfter(Instant.now()))
                throw new BadRequestException("to cannot be in the future");

            return balanceRepository.findBalanceInInterval(accountId, from, to);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public Balance findBalanceInDate(String accountId, Instant datetime) {
        try {
            Balance balance = balanceRepository.findBalanceInDate(accountId, datetime);
            return balance == null ? new Balance("", BigDecimal.ZERO, datetime, null) : balance;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<Balance> crupdateAll(List<Balance> balances) {
        try {
            return balanceRepository.saveOrUpdateAll(balances);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }
}