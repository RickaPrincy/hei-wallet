package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.CategoryMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Category;
import com.hei.wallet.heiwallet.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }
}
