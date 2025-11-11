package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqCreateBookDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateBookDTO;
import com.DatLeo.BookShop.dto.response.ResBookDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.repository.AuthorRepository;
import com.DatLeo.BookShop.repository.BookRepository;
import com.DatLeo.BookShop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryService categoryService;
    private final MinioService  minioService;

    @Value("${minio.bucket-product}")
    private String bucketProduct;

    @Value("${minio.max-file-size-bytes}")
    private Long maxFileSizeBytes;

    @Value("#{'${minio.allowed-image-mimetypes}'.split(',')}")
    private List<String> allowedImageMimetypes;

    @Override
    public ResBookDTO handleCreateBook(ReqCreateBookDTO reqCreateBookDTO) {
        boolean isCheckNameExist = this.bookRepository.existsByName(reqCreateBookDTO.getName());
        if (isCheckNameExist) {
            log.error("Thêm mới sách không thành công, {}", ApiMessage.BOOK_NAME_EXISTED);
            throw new ApiException(ApiMessage.BOOK_NAME_EXISTED);
        }
        Book book = new Book();
        book.setName(reqCreateBookDTO.getName());
        book.setPrice(reqCreateBookDTO.getPrice());
        book.setQuantity(reqCreateBookDTO.getQuantity());
        book.setDescription(reqCreateBookDTO.getDescription());
        book.setPublicationDate(reqCreateBookDTO.getPublicationDate());
        book.setImageUrl(reqCreateBookDTO.getImageUrl());

        Author author = this.authorRepository.findById(reqCreateBookDTO.getAuthorId()).orElseThrow(() -> new ApiException(ApiMessage.AUTHOR_NOT_EXIST));
        if (author != null) {
            book.setAuthor(author);
        }
        Category category = this.categoryService.handleGetCategoryById(reqCreateBookDTO.getCategoryId());
        if (category != null) {
            book.setCategory(category);
        }
        this.bookRepository.save(book);
        log.info("Thêm sách mới thành công với ID {}", book.getId());
        ResBookDTO resBookDTO = convertToResBookDTO(book);
        return resBookDTO;
    }

    @Override
    public ResBookDTO handleGetBookById(Integer id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ApiException(ApiMessage.BOOK_NOT_EXIST));
        ResBookDTO resBookDTO = convertToResBookDTO(book);
        return resBookDTO;
    }

    @Override
    public ResBookDTO handleUpdateBook(ReqUpdateBookDTO reqUpdateBookDTO) throws Exception {
        Book currentBook = this.bookRepository.findById(reqUpdateBookDTO.getId()).orElse(null);
        if (currentBook == null) {
            throw new ApiException(ApiMessage.BOOK_NOT_EXIST);
        }
        boolean isCheckNameExist = this.bookRepository.existsByName(reqUpdateBookDTO.getName());
        if (isCheckNameExist && !reqUpdateBookDTO.getName().equals(currentBook.getName())) {
            log.error("Cập nhật thông tin sách không thành công, {}", ApiMessage.BOOK_NAME_EXISTED);
            throw new ApiException(ApiMessage.BOOK_NAME_EXISTED);
        }
        currentBook.setName(reqUpdateBookDTO.getName());
        currentBook.setPrice(reqUpdateBookDTO.getPrice());
        currentBook.setQuantity(currentBook.getQuantity() + reqUpdateBookDTO.getQuantity());
        currentBook.setDescription(reqUpdateBookDTO.getDescription());
        currentBook.setPublicationDate(reqUpdateBookDTO.getPublicationDate());

        String oldImage = currentBook.getImageUrl();
        String newImage = reqUpdateBookDTO.getImageUrl();

        Author author = this.authorRepository.findById(reqUpdateBookDTO.getAuthorId()).orElseThrow(() -> new ApiException(ApiMessage.AUTHOR_NOT_EXIST));
        if (author != null) {
            currentBook.setAuthor(author);
        }
        Category category = this.categoryService.handleGetCategoryById(reqUpdateBookDTO.getCategoryId());
        if (category != null) {
            currentBook.setCategory(category);
        }

        if ((newImage == null || newImage.isBlank()) && oldImage != null && !oldImage.isBlank()) {
            minioService.deleteFromMinio(bucketProduct, oldImage);
            currentBook.setImageUrl(null);
        }
        else if (newImage != null && !newImage.isBlank() && !newImage.equals(oldImage)) {
            if (oldImage != null && !oldImage.isBlank()) {
                minioService.deleteFromMinio(bucketProduct, oldImage);
            }
            currentBook.setImageUrl(newImage);
        }

        this.bookRepository.save(currentBook);
        log.info("Cập nhật sách thành công với ID {}", currentBook.getId());
        return convertToResBookDTO(currentBook);
    }

    @Override
    public void handleDeleteBookById(Integer id) throws Exception {
        Book currentBook = this.bookRepository.findById(id).orElseThrow(() ->  new ApiException(ApiMessage.BOOK_NOT_EXIST));
        if (currentBook.getImageUrl() != null && !currentBook.getImageUrl().isBlank()) {
            minioService.deleteFromMinio(bucketProduct, currentBook.getImageUrl());
        }
        this.bookRepository.deleteById(id);
        log.info("Xóa sách thành công với ID {}", id);
    }

    @Override
    public ResPaginationDTO handleGetAllBooks(Specification<Book> spec, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Book> pageBook = this.bookRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageBook.getTotalPages());
        meta.setTotal(pageBook.getTotalElements());

        resPaginationDTO.setMeta(meta);

        List<ResBookDTO> listBookDTOs = pageBook.getContent().stream().map(item ->
                this.convertToResBookDTO(item)).collect(Collectors.toList());

        resPaginationDTO.setResult(listBookDTOs);

        return resPaginationDTO;
    }

    @Override
    public ResBookDTO convertToResBookDTO(Book book) {

        String imageUrl = null;

        if (book.getImageUrl() != null && !book.getImageUrl().isBlank()){
            try {
                imageUrl = minioService.getUrlFromMinio(bucketProduct, book.getImageUrl());

            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar author {}", book.getId(), e);
            }
        }

        ResBookDTO resBookDTO = new ResBookDTO();
        resBookDTO.setId(book.getId());
        resBookDTO.setName(book.getName());
        resBookDTO.setDescription(book.getDescription());
        resBookDTO.setSold(book.getSold());
        resBookDTO.setQuantity(book.getQuantity());
        resBookDTO.setPrice(book.getPrice());
        resBookDTO.setPublicationDate(book.getPublicationDate());
        resBookDTO.setImageUrl(imageUrl);

        ResBookDTO.AuthorDTO authorDTO = new ResBookDTO.AuthorDTO();
        authorDTO.setName(book.getAuthor().getName());
        authorDTO.setId(book.getAuthor().getId());

        ResBookDTO.CategoryDTO categoryDTO = new ResBookDTO.CategoryDTO();
        categoryDTO.setName(book.getCategory().getName());
        categoryDTO.setId(book.getCategory().getId());

        resBookDTO.setAuthor(authorDTO);
        resBookDTO.setCategory(categoryDTO);

        return resBookDTO;
    }

    @Override
    public ResUploadDTO uploadAvatar(MultipartFile imageUrl) {
        String mimeType = imageUrl.getContentType();
        long fileSize = imageUrl.getSize();
        String folderPath = "main-product";

        if (fileSize > maxFileSizeBytes) {
            throw new ApiException(ApiMessage.ERROR_FILE_SIZE);
        }
        if (!allowedImageMimetypes.contains(mimeType)) {
            throw new ApiException(ApiMessage.ERROR_FILE_MIMETYPE);
        }

        return minioService.uploadToMinio(imageUrl, bucketProduct, folderPath);
    }

    @Override
    public ResPaginationDTO handleGetCategoryBook(Integer id, Integer page, Integer size,
                                                  String sort, Double minPrice, Double maxPrice) {
        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            log.error("Danh mục không tồn tại với ID {}", id);
            throw new ApiException(ApiMessage.CATEGORY_NOT_EXIST);
        }

        Sort sortOption = Sort.unsorted();
        switch (sort) {
            case "sold":
                sortOption = Sort.by(Sort.Direction.DESC, "sold");
                break;
            case "new":
                sortOption = Sort.by(Sort.Direction.DESC, "publicationDate");
                break;
            case "priceAsc":
                sortOption = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "priceDesc":
                sortOption = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sortOption = Sort.by(Sort.Direction.DESC, "id");
        }

        Pageable pageable = PageRequest.of(page - 1, size, sortOption);
        Page<Book> bookPage = bookRepository.findByCategoryIdAndPriceRange(id, minPrice, maxPrice, pageable);

        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(bookPage.getTotalPages());
        meta.setTotal(bookPage.getTotalElements());

        List<ResBookDTO> resBookDTOS = bookPage.getContent().stream().map(item -> convertToResBookDTO(item)).toList();

        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(resBookDTOS);

        return resPaginationDTO;
    }
}
