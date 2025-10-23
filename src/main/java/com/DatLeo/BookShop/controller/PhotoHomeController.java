package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.service.PhotoHomeService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class PhotoHomeController {

    private final PhotoHomeService photoHomeService;

    @PostMapping("/upload/photo-main")
    @CustomAnnotation("Tải ảnh lên thành công.")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(photoHomeService.uploadPhotoMain(file));
    }

    @PostMapping("/upload/photo-sliders")
    @CustomAnnotation("Tải ảnh lên thành công.")
    public ResponseEntity<?> uploadPhotoSlider(@RequestParam("files") List<MultipartFile> files) throws Exception {
        return ResponseEntity.ok(photoHomeService.uploadPhotoSlider(files));
    }

    @GetMapping("/admins/upload/photo-main")
    public ResponseEntity<?> getPhotoMain() throws Exception {
        return ResponseEntity.ok(photoHomeService.getMainImage());
    }

    @GetMapping("/admins/upload/photo-sliders")
    public ResponseEntity<?> getPhotoSlider() throws Exception {
        return ResponseEntity.ok(photoHomeService.getSliderImages());
    }
}
