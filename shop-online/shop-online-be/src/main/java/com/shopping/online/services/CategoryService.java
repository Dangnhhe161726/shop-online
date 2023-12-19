package com.shopping.online.services;

import com.shopping.online.dtos.CategoryDTO;
import com.shopping.online.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    Category getCategoryById(Long id);
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    Category deleteCategory(Long id);

}
