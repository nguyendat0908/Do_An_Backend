package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadImage(MultipartFile file) throws IOException, StorageException;
}
