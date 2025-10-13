package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Override
    public ResUploadDTO uploadToMinio(MultipartFile imageUrl, String bucketName, String folderPath) {
        if (imageUrl.isEmpty()) throw new ApiException(ApiMessage.FILE_EMPTY);

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            String uniqueFileName = UUID.randomUUID() + "-" + imageUrl.getOriginalFilename();
            String objectName = folderPath + "/" + uniqueFileName;

            try (InputStream is = imageUrl.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(is, imageUrl.getSize(), -1)
                                .contentType(imageUrl.getContentType())
                                .build()
                );
            }

            ResUploadDTO uploadToMinio = new ResUploadDTO();
            uploadToMinio.setUrl(objectName);

            return uploadToMinio;
        } catch (Exception e) {
            throw new RuntimeException("Upload thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUrlFromMinio(String bucketName, String fileName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(7, TimeUnit.DAYS)
                        .build()
        );
    }

    @Override
    public void deleteFromMinio(String bucketName, String fileName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    @Override
    public InputStream downloadFromMinio(String bucketName, String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
    }

    @Override
    public List<String> listSlideImagesForFile(String type, String bucketName) {
        List<String> urls = new ArrayList<>();
        try {
            String prefix = (type == null || type.isBlank()) ? "" : type + "/";

            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir()) continue;

                String objectName = item.objectName().toLowerCase();
                if (objectName.endsWith(".jpg") || objectName.endsWith(".jpeg") ||
                        objectName.endsWith(".png") || objectName.endsWith(".gif")) {

                    String imageUrl = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .method(Method.GET)
                                    .expiry(7, TimeUnit.DAYS)
                                    .build()
                    );
                    urls.add(imageUrl);
                }
            }
        } catch (Exception e) {
            log.error("Error listing {} images: ", type, e);
        }
        urls.sort(String::compareTo);
        return urls;
    }
}
