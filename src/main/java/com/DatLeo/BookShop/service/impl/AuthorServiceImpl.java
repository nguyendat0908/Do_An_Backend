package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqCreateAuthorDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResAuthorDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.entity.Author;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.repository.AuthorRepository;
import com.DatLeo.BookShop.repository.BookRepository;
import com.DatLeo.BookShop.service.AuthorService;
import com.DatLeo.BookShop.service.MinioService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final MinioService minioService;

    @Value("${minio.bucket-avatar}")
    private String bucketAvatar;

    @Value("${minio.max-file-size-bytes}")
    private Long maxFileSizeBytes;

    @Value("#{'${minio.allowed-image-mimetypes}'.split(',')}")
    private List<String> allowedImageMimetypes;

    @Override
    public ResAuthorDTO handleCreateAuthor(ReqCreateAuthorDTO reqCreateAuthorDTO) throws IOException, StorageException {

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
        author.setImageUrl(reqCreateAuthorDTO.getImageUrl());

        log.info("Lưu tác giả thành công với tên {}", reqCreateAuthorDTO.getName());
        this.authorRepository.save(author);
        return convertToRes(author);
    }

    @Override
    public ResAuthorDTO handleGetAuthorById(Integer id) {
        Author optionalAuthor = this.authorRepository.findById(id).orElseThrow(() -> new ApiException(ApiMessage.AUTHOR_NOT_EXIST));
        return convertToRes(optionalAuthor);
    }

    @Override
    public ResAuthorDTO handleUpdateAuthor(ReqUpdateAuthorDTO req) throws Exception {
        Author currentAuthor = this.authorRepository.findById(req.getId()).orElseThrow(() -> new ApiException(ApiMessage.AUTHOR_NOT_EXIST));

        currentAuthor.setName(req.getName());
        currentAuthor.setAddress(req.getAddress());
        currentAuthor.setType(req.getType());
        currentAuthor.setDescription(req.getDescription());

        String oldImage = currentAuthor.getImageUrl();
        String newImage = req.getImageUrl();

        if ((newImage == null || newImage.isBlank()) && oldImage != null && !oldImage.isBlank()) {
            minioService.deleteFromMinio(bucketAvatar, oldImage);
            currentAuthor.setImageUrl(null);
        }
        else if (newImage != null && !newImage.isBlank() && !newImage.equals(oldImage)) {
            if (oldImage != null && !oldImage.isBlank()) {
                minioService.deleteFromMinio(bucketAvatar, oldImage);
            }
            currentAuthor.setImageUrl(newImage);
        }

        this.authorRepository.save(currentAuthor);

        log.info("Cập nhật tác giả thành công {}", currentAuthor);
        return convertToRes(currentAuthor);
    }

    @Override
    public void handleDeleteAuthor(Integer id) {
        Author currentAuthor = this.authorRepository.findById(id).orElseThrow(() -> new ApiException(ApiMessage.AUTHOR_NOT_EXIST));
        if (currentAuthor.getImageUrl() != null && !currentAuthor.getImageUrl().isBlank()){
            try {
                minioService.deleteFromMinio(bucketAvatar, currentAuthor.getImageUrl());
            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar author {}", currentAuthor.getId(), e);
            }
        }

        List<Book> books = currentAuthor.getBooks();
        bookRepository.deleteAll(books);

        authorRepository.deleteById(id);
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

        String imageUrl = null;

        if (author.getImageUrl() != null && !author.getImageUrl().isBlank()){
            try {
                imageUrl = minioService.getUrlFromMinio(bucketAvatar, author.getImageUrl());

            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar author {}", author.getId(), e);
            }
        }

        ResAuthorDTO resAuthorDTO = ResAuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .description(author.getDescription())
                .address(author.getAddress())
                .type(author.getType())
                .totalBooks(this.bookRepository.countBooksByAuthorId(author.getId()))
                .imageUrl(imageUrl)
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();

        return resAuthorDTO;
    }

    @Override
    public ResUploadDTO uploadAvatar(MultipartFile imageUrl) {
        String mimeType = imageUrl.getContentType();
        long fileSize = imageUrl.getSize();
        String folderPath = "author-avatar";

        if (fileSize > maxFileSizeBytes) {
            throw new ApiException(ApiMessage.ERROR_FILE_SIZE);
        }
        if (!allowedImageMimetypes.contains(mimeType)) {
            throw new ApiException(ApiMessage.ERROR_FILE_MIMETYPE);
        }

        return minioService.uploadToMinio(imageUrl, bucketAvatar, folderPath);
    }

    @Override
    public List<ResAuthorDTO> getListAuthors() {
        List<Author> authorList = this.authorRepository.findAll();
        List<ResAuthorDTO> authorDTOS = authorList.stream().map(iterm -> this.convertToRes(iterm)).collect(Collectors.toList());
        return authorDTOS;
    }
}
