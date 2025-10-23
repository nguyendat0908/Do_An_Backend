package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.exception.FieldException;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.service.AuthorService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/authors")
    @CustomAnnotation("Thêm mới tác giả thành công.")
    public ResponseEntity<?> createAuthor(@Valid @ModelAttribute ReqCreateAuthorDTO req, BindingResult bindingResult) throws IOException, StorageException {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.authorService.handleCreateAuthor(req));
    }

    @GetMapping("/authors/{id}")
    @CustomAnnotation("Thông tin chi tiết tác giả.")
    public ResponseEntity<?> getAuthorById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.authorService.handleGetAuthorById(id));
    }

    @PutMapping("/authors")
    @CustomAnnotation("Cập nhật thông tin tác giả thành công.")
    public ResponseEntity<?> updateAuthor(@Valid @ModelAttribute ReqUpdateAuthorDTO req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.authorService.handleUpdateAuthor(req));
    }

    @GetMapping("/admins/authors")
    @CustomAnnotation("Hiển thị danh sách thông tin tác giả.")
    public ResponseEntity<?> getListAuthors(@Filter Specification<Author> spec, Pageable pageable) {
        return ResponseEntity.ok(this.authorService.handleGetAuthors(spec, pageable));
    }

    @DeleteMapping("/authors/{id}")
    @CustomAnnotation("Xóa người dùng thành công")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Integer id) {
        this.authorService.handleDeleteAuthor(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/authors/upload")
    @CustomAnnotation("Upload ảnh thành công.")
    public ResponseEntity<?> uploadAvatar(@RequestParam("imageUrl") MultipartFile imageUrl) {
        return ResponseEntity.ok(this.authorService.uploadAvatar(imageUrl));
    }

    @GetMapping("/list-authors")
    @CustomAnnotation("Danh sách thông tin tác giả.")
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(this.authorService.getListAuthors());
    }
}
