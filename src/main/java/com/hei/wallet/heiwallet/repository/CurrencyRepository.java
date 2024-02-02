package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Currency;
import org.springframework.stereotype.Repository;


@Repository
public class CurrencyRepository extends FJPARepository<Currency> {
    public CurrencyRepository(StatementWrapper statementWrapper){
        super(Currency.class, statementWrapper);
    }
}