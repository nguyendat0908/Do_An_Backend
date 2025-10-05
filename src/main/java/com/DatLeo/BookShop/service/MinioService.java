package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioService {

    ResUploadDTO uploadToMinio(MultipartFile imageUrl, String bucketName, String folderPath);
    String getUrlFromMinio(String bucketName, String fileName) throws Exception;
    void deleteFromMinio(String bucketName, String fileName) throws Exception;
    InputStream downloadFromMinio(String bucketName, String fileName) throws Exception;
    List<String> listSlideImagesForFile(String type, String bucketName);
}
