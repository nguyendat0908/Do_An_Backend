package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoHomeService {

    ResUploadDTO uploadPhotoMain(MultipartFile imageUrl) throws Exception;
    List<ResUploadDTO> uploadPhotoSlider(List<MultipartFile> imageUrl) throws Exception;
    ResUploadDTO getMainImage() throws Exception;
    List<ResUploadDTO> getSliderImages() throws Exception;
}
