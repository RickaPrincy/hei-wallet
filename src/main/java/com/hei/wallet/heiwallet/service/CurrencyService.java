package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Currency;
import com.hei.wallet.heiwallet.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> getAll(){
        try {
            return currencyRepository.findAll();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public List<Currency> saveOrUpdateAll(List<Currency> currencies){
        try {
            return currencyRepository.saveOrUpdateAll(currencies);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public Currency findById(String id){
        try {
            return currencyRepository.findById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
}
