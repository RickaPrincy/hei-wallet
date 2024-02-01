package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Balance;
import org.springframework.stereotype.Repository;

@Repository
public class BalanceRepository extends FJPARepository<Balance> {
    public BalanceRepository(StatementWrapper statementWrapper) {
        super(Balance.class, statementWrapper);
    }
}
