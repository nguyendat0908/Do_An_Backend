package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResUserCartDTO;
import com.DatLeo.BookShop.entity.CartDetail;

public interface CartDetailService {
    ResUserCartDTO.BookDetail convertToBookDetail(CartDetail cartDetail) throws Exception;

    void handleRemoveCartDetailItem(Integer cartDetailId);

    void handleUpdateCartDetailItem(Integer cartDetailId, Integer quantityChange);
}
