package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toRest(com.hei.wallet.heiwallet.model.Category category){
        return new Category(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }

    public com.hei.wallet.heiwallet.model.Category toDomain(Category category){
        return new com.hei.wallet.heiwallet.model.Category(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }

    public CategoryMapper() {
    }
}