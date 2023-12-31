package com.shopping.online.services.category;

import com.shopping.online.dtos.CategoryDTO;
import com.shopping.online.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategory();
    Category getCategoryById(Long id);
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id) throws Exception;

}
