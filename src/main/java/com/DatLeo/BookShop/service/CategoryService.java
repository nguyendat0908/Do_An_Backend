package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResCategoryDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CategoryService {

    Category handleCreateCategory(Category category);
    Category handleUpdateCategory(Category category);
    ResPaginationDTO handleGetCategories(Specification<Category> spec, Pageable pageable);
    Category handleGetCategoryById(Integer id);
    void handleDeleteCategory(Integer id);
    ResCategoryDTO convertToDo(Category category);
    List<ResCategoryDTO> getCategories();
}
