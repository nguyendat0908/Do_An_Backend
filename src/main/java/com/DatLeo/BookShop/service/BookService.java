package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateBookDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateBookDTO;
import com.DatLeo.BookShop.dto.response.ResBookDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    ResBookDTO handleCreateBook(ReqCreateBookDTO reqCreateBookDTO);

    ResBookDTO handleGetBookById(Integer id);

    ResBookDTO handleUpdateBook(ReqUpdateBookDTO reqUpdateBookDTO) throws Exception;

    void handleDeleteBookById(Integer id) throws Exception;

    ResPaginationDTO handleGetAllBooks(Specification<Book> spec, Pageable pageable);

    ResBookDTO convertToResBookDTO(Book book);

    ResUploadDTO uploadAvatar(MultipartFile imageUrl);

    ResPaginationDTO handleGetCategoryBook(Integer id, Integer page, Integer size,
                                           String sort, Double minPrice, Double maxPrice);

    ResPaginationDTO handleSearchBook(List<Integer> categoryIds,
                                      List<Integer> authorIds,
                                      Double minPrice,
                                      Double maxPrice,
                                      String keyword,
                                      String sort,
                                      Integer page, Integer size);

    List<ResBookDTO> handleGetBooksMaxView();

    List<ResBookDTO> handleGetBooksMaxSold();

    List<ResBookDTO> handleGetBooksNewPubDate();

    List<ResBookDTO> handleGetBooksByAuthorIdAndView(Integer authorId);
}
