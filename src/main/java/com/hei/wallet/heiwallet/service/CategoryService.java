package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Category;
import com.hei.wallet.heiwallet.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public List<Category> saveOrUpdateAll(List<Category> categories){
        try {
            return categoryRepository.saveOrUpdateAll(categories);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public Category findById(String categoryId){
        try {
            return categoryRepository.findById(categoryId);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
