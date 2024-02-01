package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAll(){
        return categoryService.getAll();
    }
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
