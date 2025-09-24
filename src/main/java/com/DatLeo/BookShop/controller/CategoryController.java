package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.response.ResCategoryDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.service.CategoryService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @CustomAnnotation("Thêm mới danh mục thành công!")
    public ResponseEntity<ResCategoryDTO> createCategory(@Valid @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.convertToDo(categoryService.handleCreateCategory(category)));
    }

    @GetMapping("/categories/{id}")
    @CustomAnnotation("Thông tin chi tiết danh mục!")
    public ResponseEntity<ResCategoryDTO> getCategoryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoryService.convertToDo(categoryService.handleGetCategoryById(id)));
    }

    @PutMapping("/categories")
    @CustomAnnotation("Cập nhật thông tin danh mục thành công!")
    public ResponseEntity<ResCategoryDTO> updateCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.convertToDo(categoryService.handleUpdateCategory(category)));
    }

    @GetMapping("/categories")
    @CustomAnnotation("Hiển thị danh sách thông tin danh mục!")
    public ResponseEntity<ResPaginationDTO> getListCategories(@Filter Specification<Category> spec, Pageable pageable) {
        return ResponseEntity.ok(this.categoryService.handleGetCategories(spec, pageable));
    }

    @DeleteMapping("/categories/{id}")
    @CustomAnnotation("Xóa danh mục thành công!")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Integer id) {
        this.categoryService.handleDeleteCategory(id);
        return ResponseEntity.ok(null);
    }
}
