package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyValueRepository extends FJPARepository<CurrencyValue> {
    public CurrencyValueRepository(StatementWrapper statementWrapper) {
        super(CurrencyValue.class, statementWrapper);
    }
}
