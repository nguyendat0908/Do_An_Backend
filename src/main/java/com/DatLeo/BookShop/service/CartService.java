package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqAddItemCart;
import com.DatLeo.BookShop.dto.response.ResUserCartDTO;

public interface CartService {

    void handleAddCart(ReqAddItemCart req);

    ResUserCartDTO handleGetCart();
}
