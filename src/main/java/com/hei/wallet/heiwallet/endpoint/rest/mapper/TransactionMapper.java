package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.CreateTransaction;
import com.hei.wallet.heiwallet.endpoint.rest.model.Transaction;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.service.AccountService;
import com.hei.wallet.heiwallet.service.CategoryService;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransactionMapper {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final AccountService accountService;

    public TransactionMapper(CategoryService categoryService, CategoryMapper categoryMapper, AccountService accountService) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.accountService = accountService;
    }

    public com.hei.wallet.heiwallet.model.Transaction createToDomain(CreateTransaction transaction){
        Category category = categoryService.findById(transaction.getCategoryId());
        Account account = accountService.findById(transaction.getAccountId());

        return new com.hei.wallet.heiwallet.model.Transaction(
                transaction.getId(),
                transaction.getLabel(),
                transaction.getAmount(),
                Instant.now(),
                transaction.getType(),
                category,
                account
        );
    }

    public Transaction toRest(com.hei.wallet.heiwallet.model.Transaction transaction){
        return new Transaction(
                transaction.getId(),
                transaction.getLabel(),
                transaction.getTransactionDatetime(),
                transaction.getAmount(),
                transaction.getType(),
                categoryMapper.toRest(categoryService.findById(transaction.getCategory().getId()))
        );
    }
}
