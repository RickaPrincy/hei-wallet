package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAll(){
        try {
            return categoryRepository.findAll();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
