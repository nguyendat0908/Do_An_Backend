package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;

public interface AuthorService {

    Author handleCreateAuthor(ReqCreateAuthorDTO reqCreateAuthorDTO) throws IOException, StorageException;

    Author handleGetAuthorById(Integer id);

    Author handleUpdateAuthor(ReqUpdateAuthorDTO reqUpdateAuthorDTO) throws IOException, StorageException;

    void handleDeleteAuthor(Integer id);

    ResPaginationDTO handleGetAuthors(Specification<Author> spec, Pageable pageable);

    ResAuthorDTO convertToRes (Author author);
}
