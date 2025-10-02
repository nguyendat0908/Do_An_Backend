package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateBookDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateBookDTO;
import com.DatLeo.BookShop.dto.response.ResBookDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.exception.FieldException;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.service.BookService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    @CustomAnnotation("Thêm mới sách thành công.")
    public ResponseEntity<ResBookDTO> createBook(@Valid @ModelAttribute ReqCreateBookDTO req, BindingResult bindingResult) throws IOException, StorageException {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.bookService.handleCreateBook(req));
    }

    @GetMapping("/books/{id}")
    @CustomAnnotation("Thông tin chi tiết sách.")
    public ResponseEntity<ResBookDTO> getBookById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.bookService.handleGetBookById(id));
    }

    @GetMapping("/books")
    @CustomAnnotation("Hiển thị danh sách thông tin sách.")
    public ResponseEntity<ResPaginationDTO> getBookById(@Filter Specification<Book> spec, Pageable pageable) {
        return ResponseEntity.ok(this.bookService.handleGetAllBooks(spec, pageable));
    }

    @PutMapping("/books")
    @CustomAnnotation("Cập nhật thông tin sách thành công.")
    public ResponseEntity<ResBookDTO> updateBook(@ModelAttribute ReqUpdateBookDTO req, BindingResult bindingResult) throws IOException, StorageException {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.bookService.handleUpdateBook(req));
    }

    @DeleteMapping("/books/{id}")
    @CustomAnnotation("Xóa sách thành công.")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Integer id) {
        this.bookService.handleDeleteBookById(id);
        return ResponseEntity.ok(null);
    }
}
