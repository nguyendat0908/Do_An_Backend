package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.dto.response.ResponseDTO;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.service.FileService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class FileController {

    private final FileService fileService;

    public FileController (FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @CustomAnnotation("Upload ảnh thành công!")
    public ResponseEntity<ResponseDTO<ResUploadDTO>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException, StorageException {
        return ResponseEntity.ok(new ResponseDTO<>(this.fileService.uploadImage(file)));
    }
}
