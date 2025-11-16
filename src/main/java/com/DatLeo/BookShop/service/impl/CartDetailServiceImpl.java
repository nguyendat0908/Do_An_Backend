package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResUserCartDTO;
import com.DatLeo.BookShop.entity.CartDetail;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.CartDetailRepository;
import com.DatLeo.BookShop.service.CartDetailService;
import com.DatLeo.BookShop.service.MinioService;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartDetailServiceImpl implements CartDetailService {

    @Value("${minio.bucket-product}")
    private String bucketProduct;

    private final CartDetailRepository cartDetailRepository;
    private final MinioService  minioService;

    @Override
    public ResUserCartDTO.BookDetail convertToBookDetail(CartDetail cartDetail) throws Exception {
        ResUserCartDTO.BookDetail bookDetail = new ResUserCartDTO.BookDetail();
        bookDetail.setId(cartDetail.getBook().getId());
        bookDetail.setBookName(cartDetail.getBook().getName());
        bookDetail.setBookAuthor(cartDetail.getBook().getAuthor().getName());
        bookDetail.setBookPrice(cartDetail.getBook().getPrice());
        bookDetail.setBookQuantity(cartDetail.getQuantity());
        bookDetail.setCartDetailId(cartDetail.getId());

        String imageUrl = minioService.getUrlFromMinio(bucketProduct, cartDetail.getBook().getImageUrl());
        bookDetail.setBookImage(imageUrl);

        return bookDetail;
    }

    @Override
    public void handleRemoveCartDetailItem(Integer cartDetailId) {
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ApiException(ApiMessage.NOT_CART));
        if (cartDetail != null) {
            cartDetailRepository.deleteById(cartDetailId);
        }
    }

    @Override
    public void handleUpdateCartDetailItem(Integer cartDetailId, Integer quantityChange) {
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ApiException(ApiMessage.NOT_CART));
        if (cartDetail != null) {
            cartDetail.setQuantity(cartDetail.getQuantity() + quantityChange);
            cartDetailRepository.save(cartDetail);
        }
    }
}
