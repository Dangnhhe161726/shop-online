package com.shopping.online.services;

import com.shopping.online.dtos.CategoryDTO;
import com.shopping.online.models.Category;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    Category getCategoryById(Long id);
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id) throws Exception;

}
