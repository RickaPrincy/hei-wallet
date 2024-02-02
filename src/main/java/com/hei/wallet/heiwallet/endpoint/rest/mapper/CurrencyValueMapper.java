package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.CreateCurrencyValue;
import com.hei.wallet.heiwallet.endpoint.rest.model.Currency;
import com.hei.wallet.heiwallet.endpoint.rest.model.CurrencyValue;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.exception.NotFoundException;
import com.hei.wallet.heiwallet.repository.CurrencyRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class CurrencyValueMapper {
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    public com.hei.wallet.heiwallet.model.CurrencyValue createToDomain(CreateCurrencyValue currency){
        try{
            final com.hei.wallet.heiwallet.model.Currency source = currencyRepository.findById(currency.getSourceId());
            final com.hei.wallet.heiwallet.model.Currency destination = currencyRepository.findById(currency.getDestinationId());

            if(source == null || destination == null){
                throw new NotFoundException("Destination or source currency are not found");
            }

            return new com.hei.wallet.heiwallet.model.CurrencyValue(
                    currency.getId(),
                    currency.getEffectiveDatetime(),
                    currency.getAmount(),
                    source,
                    destination
            );
        }catch(SQLException error){
            throw new InternalServerErrorException();
        }
    }

    public CurrencyValue toRest(com.hei.wallet.heiwallet.model.CurrencyValue currencyValue){
        if(currencyValue == null)
            return null;
        return new CurrencyValue(
                currencyValue.getId(),
                currencyValue.getEffectiveDatetime(),
                currencyValue.getAmount(),
                currencyMapper.toRest(currencyValue.getSource()),
                currencyMapper.toRest(currencyValue.getDestination())
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

    public CurrencyValueMapper(CurrencyMapper currencyMapper, CurrencyRepository currencyRepository) {
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
    }
}