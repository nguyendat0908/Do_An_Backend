package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqAddItemCart;
import com.DatLeo.BookShop.service.CartService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart/add-items")
    @CustomAnnotation("Thêm vào giỏ hàng thành công.")
    public ResponseEntity<Void> addCart(@RequestBody ReqAddItemCart req) {
        cartService.handleAddCart(req);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/carts")
    @CustomAnnotation("Hiển thị thông tin giỏ hàng.")
    public ResponseEntity<?> getDetailCart() {
        return ResponseEntity.ok(cartService.handleGetCart());
    }
}
