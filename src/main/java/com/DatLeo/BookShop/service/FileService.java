package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    ResUploadDTO uploadImage(MultipartFile file) throws IOException, StorageException;

    void deleteImage(String publicId);
}
