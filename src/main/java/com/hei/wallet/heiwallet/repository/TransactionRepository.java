package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionRepository extends FJPARepository<Transaction> {
    public TransactionRepository(StatementWrapper statementWrapper) {
        super(Transaction.class, statementWrapper);
    }

    @Override
    protected Object getAttributeValue(Transaction transaction, Attribute attribute) {
        return switch(attribute.getFieldName()){
            case "account" -> transaction.getAccount().getId();
            case "category" -> transaction.getCategory().getId();
            default -> super.getAttributeValue(transaction, attribute);
        };
    }

    @Override
    protected Transaction mapResultSetToInstance(ResultSet resultSet, List<Class<?>> excludes) {
        Transaction transaction = super.mapResultSetToInstance(resultSet, excludes);

        try {
            // category relation
            CategoryRepository categoryRepository = new CategoryRepository(statementWrapper);
            Category category = categoryRepository.findById(resultSet.getString("category"));
            transaction.setCategory(category);

            // account relation
            if(excludes.contains(Account.class))
                return transaction;
            AccountRepository accountRepository = new AccountRepository(statementWrapper);
            Account account = accountRepository.findById(
                    resultSet.getString("account"),
                    List.of(Account.class)
            );
            transaction.setAccount(account);
            return transaction;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}