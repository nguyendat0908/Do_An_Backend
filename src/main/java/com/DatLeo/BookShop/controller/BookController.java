package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateBookDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateBookDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.exception.FieldException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    @CustomAnnotation("Thêm mới sách thành công.")
    public ResponseEntity<?> createBook(@Valid @ModelAttribute ReqCreateBookDTO req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.bookService.handleCreateBook(req));
    }

    @GetMapping("/books/{id}")
    @CustomAnnotation("Thông tin chi tiết sách.")
    public ResponseEntity<?> getBookById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.bookService.handleGetBookById(id));
    }

    @GetMapping("/admins/books")
    @CustomAnnotation("Hiển thị danh sách thông tin sách.")
    public ResponseEntity<?> getBooks(@Filter Specification<Book> spec, Pageable pageable) {
        return ResponseEntity.ok(this.bookService.handleGetAllBooks(spec, pageable));
    }

    @GetMapping("/users/books")
    @CustomAnnotation("Hiển thị danh sách thông tin sách.")
    public ResponseEntity<?> getBooksUser(@Filter Specification<Book> spec, Pageable pageable) {
        return ResponseEntity.ok(this.bookService.handleGetAllBooks(spec, pageable));
    }

    @PutMapping("/books")
    @CustomAnnotation("Cập nhật thông tin sách thành công.")
    public ResponseEntity<?> updateBook(@Valid @ModelAttribute ReqUpdateBookDTO req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(this.bookService.handleUpdateBook(req));
    }

    @DeleteMapping("/books/{id}")
    @CustomAnnotation("Xóa sách thành công.")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Integer id) throws Exception {
        this.bookService.handleDeleteBookById(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/books/upload")
    @CustomAnnotation("Upload ảnh thành công.")
    public ResponseEntity<?> uploadAvatar(@RequestParam("imageUrl") MultipartFile imageUrl) {
        return  ResponseEntity.ok(this.bookService.uploadAvatar(imageUrl));
    }

    @GetMapping("/books/category/{id}")
    @CustomAnnotation("Danh sách sách theo danh mục.")
    public ResponseEntity<?> getBooksByCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "all") String sort,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(bookService.handleGetCategoryBook(id, page, size, sort, minPrice, maxPrice));
    }

    @GetMapping("/books/search")
    @CustomAnnotation("Tìm kiếm sách theo từ khóa.")
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) List<Integer> authorIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "all") String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "25") Integer size) {
        return ResponseEntity.ok(bookService.handleSearchBook(categoryIds, authorIds, minPrice, maxPrice, keyword, sort, page, size));
    }

    @GetMapping("/books/top-view")
    @CustomAnnotation("Hiển thị danh sách sách có lượt xem cao nhất.")
    public ResponseEntity<?> getBooksByMaxView() {
        return ResponseEntity.ok(bookService.handleGetBooksMaxView());
    }

    @GetMapping("/books/top-sold")
    @CustomAnnotation("Hiển thị danh sách sách có lượt mua cao nhất.")
    public ResponseEntity<?> getBooksByMaxSold() {
        return ResponseEntity.ok(bookService.handleGetBooksMaxSold());
    }

    @GetMapping("/books/new-pub-date")
    @CustomAnnotation("Hiển thị danh sách sách mới nhất.")
    public ResponseEntity<?> getBooksByNewPubDate() {
        return ResponseEntity.ok(bookService.handleGetBooksNewPubDate());
    }

    @GetMapping("/books/suggest")
    @CustomAnnotation("Hiển thị gợi ý danh sách sách.")
    public ResponseEntity<?> getBooksSuggest(@RequestParam("authorId") Integer authorId) {
        return ResponseEntity.ok(bookService.handleGetBooksByAuthorIdAndView(authorId));
    }

    @GetMapping("/books/view-history")
    @CustomAnnotation("Hiển thị danh sách sách đã xem gần đây.")
    public ResponseEntity<?> getViewRecentlyBooks() {
        return ResponseEntity.ok(bookService.handleGetRecentlyViewBooks());
    }
}
