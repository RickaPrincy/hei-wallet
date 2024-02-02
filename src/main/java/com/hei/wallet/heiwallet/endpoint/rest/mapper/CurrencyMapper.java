package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {
    public Currency toRest(com.hei.wallet.heiwallet.model.Currency currency){
        return new Currency(
                currency.getId(),
                currency.getName(),
                currency.getCode()
        );
    }

    public com.hei.wallet.heiwallet.model.Currency toDomain(Currency currency){
        return new com.hei.wallet.heiwallet.model.Currency(
                currency.getId(),
                currency.getName(),
                currency.getCode(),
                null
        );
    }

    public CurrencyMapper() {
    }
}