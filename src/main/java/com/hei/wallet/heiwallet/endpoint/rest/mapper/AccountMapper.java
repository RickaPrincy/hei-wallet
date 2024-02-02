package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.*;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.exception.NotFoundException;
import com.hei.wallet.heiwallet.repository.CurrencyRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class AccountMapper {
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    public com.hei.wallet.heiwallet.model.Account createToDomain(CreateAccount account){
        try{
            final com.hei.wallet.heiwallet.model.Currency currency = currencyRepository.findById(account.getCurrencyId());

            if(currency == null){
                throw new NotFoundException("Destination or source currency are not found");
            }

            return new com.hei.wallet.heiwallet.model.Account(
                    account.getId(),
                    account.getName(),
                    account.getType(),
                    currency
            );
        }catch(SQLException error){
            throw new InternalServerErrorException();
        }
    }

    public Account toRest(com.hei.wallet.heiwallet.model.Account account){
        return new Account(
                account.getId(),
                account.getName(),
                account.getType(),
                currencyMapper.toRest(account.getCurrency())
        );
    }

    public com.hei.wallet.heiwallet.model.Account toDomain(Account account){
        return new com.hei.wallet.heiwallet.model.Account(
                account.getId(),
                account.getName(),
                account.getType(),
                null
        );
    }

    public AccountMapper(CurrencyMapper currencyMapper, CurrencyRepository currencyRepository) {
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
    }
}