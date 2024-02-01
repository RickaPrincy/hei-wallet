package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository extends FJPARepository<Category> {
    public CategoryRepository(StatementWrapper statementWrapper) {
        super(Category.class, statementWrapper);
    }
}
