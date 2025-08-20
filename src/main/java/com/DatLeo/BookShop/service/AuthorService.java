package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.exception.StorageException;

import java.io.IOException;

public interface AuthorService {

    Author handleCreateAuthor(ReqCreateAuthorDTO reqCreateAuthorDTO) throws IOException, StorageException;

    Author handleGetAuthorById(Integer id);

    Author handleUpdateAuthor(ReqUpdateAuthorDTO reqUpdateAuthorDTO) throws IOException, StorageException;

    void handleDeleteAuthor(Integer id);

    ResAuthorDTO convertToRes (Author author);
}
