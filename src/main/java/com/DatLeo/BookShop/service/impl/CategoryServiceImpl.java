package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResBookDTO;
import com.DatLeo.BookShop.dto.response.ResCategoryDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.BookRepository;
import com.DatLeo.BookShop.repository.CategoryRepository;
import com.DatLeo.BookShop.repository.DiscountRepository;
import com.DatLeo.BookShop.service.BookService;
import com.DatLeo.BookShop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    private final DiscountRepository discountRepository;

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
            currentCategory.setName(category.getName() != null ? category.getName() : currentCategory.getName());
            currentCategory.setDescription(category.getDescription()  != null ? category.getDescription() : currentCategory.getDescription());
        }
        return this.categoryRepository.save(currentCategory);
    }

    @Override
    public ResPaginationDTO handleGetCategories(Specification<Category> spec, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Category> pageCategory = this.categoryRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCategory.getTotalPages());
        meta.setTotal(pageCategory.getTotalElements());

        resPaginationDTO.setMeta(meta);

        List<ResCategoryDTO> listCategoryDTOs = pageCategory.stream().map(item -> convertToDo(item)).toList();

        resPaginationDTO.setResult(listCategoryDTOs);

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
    @Transactional
    public void handleDeleteCategory(Integer id) {
        Category category = handleGetCategoryById(id);
        if (category == null) {
            log.error("Danh mục không tồn tại với ID {}", id);
            throw new ApiException(ApiMessage.CATEGORY_NOT_EXIST);
        }
        List<Book> books = category.getBooks();
        bookRepository.deleteAll(books);

        discountRepository.deleteDiscountCategory(category.getId());

        this.categoryRepository.deleteById(id);
    }

    @Override
    public ResCategoryDTO convertToDo(Category category) {
        return ResCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public List<ResCategoryDTO> getCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(item -> convertToDo(item)).toList();
    }
}
