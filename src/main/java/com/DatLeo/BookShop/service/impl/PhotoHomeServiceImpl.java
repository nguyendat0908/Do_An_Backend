package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.entity.PhotoHome;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.PhotoHomeRepository;
import com.DatLeo.BookShop.service.MinioService;
import com.DatLeo.BookShop.service.PhotoHomeService;
import com.DatLeo.BookShop.util.constant.PhotoType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoHomeServiceImpl implements PhotoHomeService {

    private final MinioService minioService;
    private final PhotoHomeRepository photoHomeRepository;

    @Value("${minio.bucket-slider}")
    private String bucketSlider;

    @Value("${minio.max-file-size-bytes}")
    private Long maxFileSizeBytes;

    @Value("#{'${minio.allowed-image-mimetypes}'.split(',')}")
    private List<String> allowedImageMimetypes;

    @Override
    @Transactional
    public ResUploadDTO uploadPhotoMain(MultipartFile imageUrl) throws Exception {
        String mimeType = imageUrl.getContentType();
        long fileSize = imageUrl.getSize();
        String folderPath = "main-photo";

        if (fileSize > maxFileSizeBytes) {
            throw new ApiException(ApiMessage.ERROR_FILE_SIZE);
        }
        if (!allowedImageMimetypes.contains(mimeType)) {
            throw new ApiException(ApiMessage.ERROR_FILE_MIMETYPE);
        }
        PhotoHome photoHome = photoHomeRepository.findByType(PhotoType.MAIN);
        if (photoHome != null) {
            photoHomeRepository.deleteByType(PhotoType.MAIN);
            if (photoHome.getImageUrl() != null && !photoHome.getImageUrl().isBlank())
            minioService.deleteFromMinio(bucketSlider, photoHome.getImageUrl());
        }

        ResUploadDTO res = minioService.uploadToMinio(imageUrl, bucketSlider, folderPath);

        PhotoHome newPhotoHome = new PhotoHome();
        newPhotoHome.setType(PhotoType.MAIN);
        newPhotoHome.setImageUrl(res.getUrl());
        photoHomeRepository.save(newPhotoHome);

        return res;
    }

    @Override
    @Transactional
    public List<ResUploadDTO> uploadPhotoSlider(List<MultipartFile> imageFiles) throws Exception {
        String folderPath = "slider-photos";
        List<ResUploadDTO> uploadedFiles = new ArrayList<>();

        List<PhotoHome> oldPhotos = photoHomeRepository.findAllByType(PhotoType.SLIDER);
        for (PhotoHome old : oldPhotos) {
            minioService.deleteFromMinio(bucketSlider, old.getImageUrl());
        }
        photoHomeRepository.deleteAll(oldPhotos);

        for (MultipartFile file : imageFiles) {
            String mimeType = file.getContentType();
            long fileSize = file.getSize();

            if (fileSize > maxFileSizeBytes) {
                throw new ApiException(ApiMessage.ERROR_FILE_SIZE);
            }
            if (!allowedImageMimetypes.contains(mimeType)) {
                throw new ApiException(ApiMessage.ERROR_FILE_MIMETYPE);
            }

            ResUploadDTO res = minioService.uploadToMinio(file, bucketSlider, folderPath);

            PhotoHome newPhotoHome = new PhotoHome();
            newPhotoHome.setType(PhotoType.SLIDER);
            newPhotoHome.setImageUrl(res.getUrl());
            photoHomeRepository.save(newPhotoHome);

            uploadedFiles.add(res);
        }

        return uploadedFiles;
    }

    @Override
    public ResUploadDTO getMainImage() throws Exception {
        PhotoHome photoHome = photoHomeRepository.findByType(PhotoType.MAIN);
        String resUrl = minioService.getUrlFromMinio(bucketSlider, photoHome.getImageUrl());
        ResUploadDTO res = new ResUploadDTO();
        res.setUrl(resUrl);
        return res;
    }

    @Override
    public List<ResUploadDTO> getSliderImages() throws Exception {
        List<PhotoHome> photoHomes = photoHomeRepository.findAllByType(PhotoType.SLIDER);
        List<ResUploadDTO> res = new ArrayList<>();
        for (PhotoHome photoHome : photoHomes) {
            String resUrl = minioService.getUrlFromMinio(bucketSlider, photoHome.getImageUrl());
            ResUploadDTO resUploadDTO = new ResUploadDTO();
            resUploadDTO.setUrl(resUrl);

            res.add(resUploadDTO);
        }
        return res;
    }
}
