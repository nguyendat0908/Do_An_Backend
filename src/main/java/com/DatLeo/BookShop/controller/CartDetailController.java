package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqAddItemCart;
import com.DatLeo.BookShop.service.CartDetailService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class CartDetailController {

    private final CartDetailService cartDetailService;

    @DeleteMapping("/cart-detail/{cartDetailId}")
    @CustomAnnotation("Xóa sách khỏi giỏ hàng thành công.")
    public ResponseEntity<Void> removeItemCart(@PathVariable Integer cartDetailId) {
        cartDetailService.handleRemoveCartDetailItem(cartDetailId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/cart-detail/{cartDetailId}")
    @CustomAnnotation("Thêm sách vào giỏ hàng thành công.")
    public ResponseEntity<Void> increaseItemCart(@PathVariable Integer cartDetailId, @RequestParam Integer quantityChange) {
        cartDetailService.handleUpdateCartDetailItem(cartDetailId, quantityChange);
        return ResponseEntity.ok(null);
    }
}
