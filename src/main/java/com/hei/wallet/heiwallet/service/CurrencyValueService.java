package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.CurrencyValue;
import com.hei.wallet.heiwallet.repository.CurrencyValueRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CurrencyValueService {
    private final CurrencyValueRepository currencyValueRepository;

    public CurrencyValueService(CurrencyValueRepository currencyValueRepository) {
        this.currencyValueRepository = currencyValueRepository;
    }

    public List<CurrencyValue> getAll(){
        try {
            return currencyValueRepository.findAll();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public List<CurrencyValue> saveOrUpdateAll(List<CurrencyValue> currencyValues) {
        try {
            return currencyValueRepository.saveOrUpdateAll(currencyValues);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
