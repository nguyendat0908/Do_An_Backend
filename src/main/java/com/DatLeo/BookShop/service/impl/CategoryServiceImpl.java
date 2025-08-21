package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.CategoryRepository;
import com.DatLeo.BookShop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category handleCreateCategory(Category category) {
        boolean categoryExists = categoryRepository.existsByName(category.getName());
        if (categoryExists) {
            log.error("Tạo danh mục không thành công. {}", ApiMessage.CATEGORY_NAME_EXISTED);
            throw new ApiException(ApiMessage.CATEGORY_NAME_EXISTED);
        }
        return this.categoryRepository.save(category);
    }

    @Override
    public Category handleUpdateCategory(Category category) {
        Category currentCategory = handleGetCategoryById(category.getId());
        if (currentCategory != null) {
            currentCategory.setName(category.getName());
            currentCategory.setDescription(category.getDescription());
        }
        return this.categoryRepository.save(currentCategory);
    }

    @Override
    public ResPaginationDTO handleGetCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> pageCategory = this.categoryRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCategory.getTotalPages());
        meta.setTotal(pageCategory.getTotalElements());

        resPaginationDTO.setMeta(meta);

        List<Category> listCategoryDTOs = pageCategory.getContent();

        resPaginationDTO.setResult(listCategoryDTOs);
        log.info("Hiển thị danh sách danh mục phân trang thành công!");

        return resPaginationDTO;
    }

    @Override
    public Category handleGetCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new ApiException(ApiMessage.CATEGORY_NOT_EXIST);
        }
        return category.get();
    }

    @Override
    public void handleDeleteCategory(Integer id) {
        Category category = handleGetCategoryById(id);
        if (category == null) {
            log.error("Danh mục không tồn tại với ID {}", id);
            throw new ApiException(ApiMessage.CATEGORY_NOT_EXIST);
        }
        this.categoryRepository.deleteById(id);
    }
}
