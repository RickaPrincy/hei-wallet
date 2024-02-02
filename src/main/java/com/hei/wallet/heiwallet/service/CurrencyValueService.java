package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.CurrencyValue;
import com.hei.wallet.heiwallet.repository.CurrencyValueRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyValueService {
    private final CurrencyValueRepository currencyValueRepository;

    public CurrencyValueService(CurrencyValueRepository currencyValueRepository) {
        this.currencyValueRepository = currencyValueRepository;
    }

    public List<CurrencyValue> saveOrUpdateAll(List<CurrencyValue> currencyValues) {
        try {
            return currencyValueRepository.saveOrUpdateAll(currencyValues);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<CurrencyValue> findAllByCurrencyId(String currencyId){
        try {
            return currencyValueRepository
                    .findAllByCurrencyId(currencyId)
                    .stream().distinct().toList();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Optional<CurrencyValue> findCurrencyCurrentValue(String currencyId){
        return findAllByCurrencyId(currencyId).stream().findFirst();
    }
}
