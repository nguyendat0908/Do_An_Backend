package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.repository.AuthorRepository;
import com.DatLeo.BookShop.repository.BookRepository;
import com.DatLeo.BookShop.service.AuthorService;
import com.DatLeo.BookShop.service.FileService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final FileService fileService;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, FileService fileService) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.fileService = fileService;
    }

    @Override
    public Author handleCreateAuthor(ReqCreateAuthorDTO reqCreateAuthorDTO) throws IOException, StorageException {

        boolean isCheckByName = this.authorRepository.existsByName(reqCreateAuthorDTO.getName());
        if (isCheckByName) {
            log.error("Không lưu người dùng thành công {}", ApiMessage.AUTHOR_NAME_EXISTED);
            throw new ApiException(ApiMessage.AUTHOR_NAME_EXISTED);
        }
        Author author = new Author();
        author.setName(reqCreateAuthorDTO.getName());
        author.setDescription(reqCreateAuthorDTO.getDescription());
        author.setAddress(reqCreateAuthorDTO.getAddress());
        author.setType(reqCreateAuthorDTO.getType());

        if (reqCreateAuthorDTO.getImageUrl() != null && !reqCreateAuthorDTO.getImageUrl().isEmpty()) {
            ResUploadDTO uploadResult = this.fileService.uploadImage(reqCreateAuthorDTO.getImageUrl());
            author.setImageUrl(uploadResult.getUrl());
        }
        log.info("Lưu tác giả thành công với tên {}", reqCreateAuthorDTO.getName());
        return this.authorRepository.save(author);
    }

    @Override
    public Author handleGetAuthorById(Integer id) {
        Optional<Author> optionalAuthor = this.authorRepository.findById(id);
        if (optionalAuthor.isEmpty()) {
            log.error("Tác giả không tồn tại với ID {}", id);
            throw new ApiException(ApiMessage.AUTHOR_NOT_EXIST);
        }
        return optionalAuthor.get();
    }

    @Override
    public Author handleUpdateAuthor(ReqUpdateAuthorDTO req) throws IOException, StorageException {
        Author currentAuthor = handleGetAuthorById(req.getId());
        if (currentAuthor == null) {
            throw new ApiException(ApiMessage.AUTHOR_NOT_EXIST);
        }

        currentAuthor.setName(req.getName());
        currentAuthor.setAddress(req.getAddress());
        currentAuthor.setType(req.getType());
        currentAuthor.setDescription(req.getDescription());

        if (req.getImageUrl() != null && !req.getImageUrl().isEmpty()) {
            // Xóa ảnh cũ
            if (currentAuthor.getImagePublicId() != null) {
                this.fileService.deleteImage(currentAuthor.getImagePublicId());
            }
            ResUploadDTO resUploadDTO = this.fileService.uploadImage(req.getImageUrl());
            currentAuthor.setImageUrl(resUploadDTO.getUrl());
        }

        this.authorRepository.save(currentAuthor);

        log.info("Cập nhật tác giả thành công {}", currentAuthor);
        return currentAuthor;
    }

    @Override
    public void handleDeleteAuthor(Integer id) {
        Author author = handleGetAuthorById(id);
        if (author == null) {
            log.error("Tác giả không tồn tại với ID {}", id);
            throw new ApiException(ApiMessage.AUTHOR_NOT_EXIST);
        }
        if (author.getImageUrl() != null && !author.getImageUrl().isEmpty()) {
            this.fileService.deleteImage(author.getImagePublicId());
        }
        this.authorRepository.deleteById(id);
        log.info("Xóa tác giả thành công với ID {}", id);
    }

    @Override
    public ResPaginationDTO handleGetAuthors(Specification<Author> spec, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Author> pageAuthor = this.authorRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageAuthor.getTotalPages());
        meta.setTotal(pageAuthor.getTotalElements());

        resPaginationDTO.setMeta(meta);

        List<ResAuthorDTO> listAuthorDTOs = pageAuthor.getContent().stream().map(item ->
                this.convertToRes(item)).collect(Collectors.toList());

        resPaginationDTO.setResult(listAuthorDTOs);

        return resPaginationDTO;
    }

    @Override
    public ResAuthorDTO convertToRes(Author author) {

        ResAuthorDTO resAuthorDTO = ResAuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .description(author.getDescription())
                .address(author.getAddress())
                .type(author.getType())
                .totalBooks(this.bookRepository.countBooksByAuthorId(author.getId()))
                .imageUrl(author.getImageUrl())
                .imagePublicId(author.getImagePublicId())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();

        return resAuthorDTO;
    }
}
