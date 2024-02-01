package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.model.Currency;
import com.hei.wallet.heiwallet.repository.CategoryRepository;
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

    public List<Currency> saveOrUpdateAll(List<Currency> categories){
        try {
            return currencyRepository.saveOrUpdateAll(categories);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
}
