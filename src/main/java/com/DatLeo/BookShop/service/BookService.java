package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateBookDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateBookDTO;
import com.DatLeo.BookShop.dto.response.ResBookDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;

public interface BookService {

    ResBookDTO handleCreateBook(ReqCreateBookDTO reqCreateBookDTO) throws IOException, StorageException;

    ResBookDTO handleGetBookById(Integer id);

    ResBookDTO handleUpdateBook(ReqUpdateBookDTO reqUpdateBookDTO) throws IOException, StorageException;

    void handleDeleteBookById(Integer id);

    ResPaginationDTO handleGetAllBooks(Specification<Book> spec, Pageable pageable);

    ResBookDTO convertToResBookDTO(Book book);
}
