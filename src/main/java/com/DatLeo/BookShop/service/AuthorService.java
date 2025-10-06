package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthorService {

    ResAuthorDTO handleCreateAuthor(ReqCreateAuthorDTO reqCreateAuthorDTO) throws IOException, StorageException;

    ResAuthorDTO handleGetAuthorById(Integer id);

    ResAuthorDTO handleUpdateAuthor(ReqUpdateAuthorDTO reqUpdateAuthorDTO) throws Exception;

    void handleDeleteAuthor(Integer id);

    ResPaginationDTO handleGetAuthors(Specification<Author> spec, Pageable pageable);

    ResAuthorDTO convertToRes (Author author);

    ResUploadDTO uploadAvatar(MultipartFile imageUrl);

    List<ResAuthorDTO> getListAuthors();
}
