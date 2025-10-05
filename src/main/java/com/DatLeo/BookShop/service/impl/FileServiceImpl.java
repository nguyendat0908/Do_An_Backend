package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.service.FileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Cloudinary cloudinary;

    public FileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public ResUploadDTO uploadImage(MultipartFile file) throws IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được null hoặc rỗng");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException(
                    "File tải lên không đúng định dạng! Chỉ cho phép " + allowedExtensions.toString());
        }

        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);

        log.info("Thông tin file upload: {}", fileUpload);

        Map resUploadDTO = cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);

        String url = cloudinary.url().generate(publicValue + "." + extension);

        return new ResUploadDTO(url);
    }

    @Override
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            log.warn("Không có publicId để xóa ảnh");
            return;
        }
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Đã xóa ảnh Cloudinary với publicId {} - Kết quả: {}", publicId, result);
        } catch (IOException e) {
            log.error("Lỗi khi xóa ảnh Cloudinary với publicId {}: {}", publicId, e.getMessage());
            throw new RuntimeException("Không thể xóa ảnh trên Cloudinary", e);
        }
    }

    private File convert(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được null hoặc rỗng");
        }
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            log.info("Thông tin file path: {}", filePath);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "-", fileName);
    }

    private String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
