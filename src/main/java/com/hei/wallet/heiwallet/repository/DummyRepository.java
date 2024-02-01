package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Dummy;
import org.springframework.stereotype.Repository;

@Repository
public class DummyRepository extends FJPARepository<Dummy> {
    public DummyRepository(StatementWrapper statementWrapper) {
        super(Dummy.class, statementWrapper);
    }
}
