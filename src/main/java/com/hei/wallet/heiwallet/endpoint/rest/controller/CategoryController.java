package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.CategoryMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Category;
import com.hei.wallet.heiwallet.endpoint.rest.model.CategorySumType;
import com.hei.wallet.heiwallet.model.CategorySum;
import com.hei.wallet.heiwallet.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/categories")
    public List<Category> getAll(){
        return categoryService
                .getAll()
                .stream()
                .map(categoryMapper::toRest).toList();
    }

    @PutMapping("/categories")
    public List<Category> crupdateAll(@RequestBody List<Category> categories){
        return categoryService.saveOrUpdateAll(
                categories.stream().map(categoryMapper::toDomain).toList()
        ).stream().map(categoryMapper::toRest).toList();
    }

    @GetMapping("accounts/{accountId}/categories/sum")
    public List<CategorySum> getAllCategorySumByAccount(
            @PathVariable String accountId,
            @RequestParam Instant from,
            @RequestParam Instant to,
            @RequestParam(defaultValue = "java") CategorySumType type
    ){
        return categoryService.getAllCategorySumByAccount(accountId, from, to, type);
    }

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }
}
