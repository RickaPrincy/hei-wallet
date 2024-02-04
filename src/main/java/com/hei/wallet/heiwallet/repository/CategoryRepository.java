package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.model.CategorySum;
import com.hei.wallet.heiwallet.model.Transaction;
import com.hei.wallet.heiwallet.service.AccountService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository extends FJPARepository<Category> {
    private final AccountService accountService;
    public CategoryRepository(StatementWrapper statementWrapper, AccountService accountService) {
        super(Category.class, statementWrapper);
        this.accountService = accountService;
    }

    public List<CategorySum> getAllCategorySum(String accountId, Instant from, Instant to) throws SQLException {
        String query = "SELECT * FROM sum_in_out_by_category(?, ? ,?);";
        return statementWrapper.select(query, List.of(accountId, from, to), resultSet -> {
            try {
                String CATEGORY_LABEL = "category_name", TOTAL_AMOUNT="total_amount";
                return new CategorySum(
                    resultSet.getString(CATEGORY_LABEL),
                    resultSet.getBigDecimal(TOTAL_AMOUNT)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public List<CategorySum> getAllCategorySumWithJava(String accountId, Instant from, Instant to) throws SQLException {
        Account account = accountService.findById(accountId);
        if(account == null)
            return null;
        List<Transaction> transactions = account.getTransactions();
        HashMap<String, BigDecimal> categorySums = new HashMap<>();
        for(Category category: this.findAll()){
            categorySums.put(category.getName(), BigDecimal.valueOf(0));
        }
        transactions.forEach(el -> {
            if(
                el.getTransactionDatetime().isAfter(to) ||
                el.getTransactionDatetime().isBefore(from)
            ){
                return;
            }

            String categoryName = el.getCategory().getName();
            BigDecimal oldValue = categorySums.get(categoryName);
            categorySums.put(categoryName, oldValue.add(el.getAmount()));
        });
        return categorySums
                .entrySet()
                .stream().map(el -> new CategorySum(el.getKey(), el.getValue()))
                .toList();
    }
}
