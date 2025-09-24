package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqUpdateDiscount;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Discount;
import com.DatLeo.BookShop.service.DiscountService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/discounts")
    @CustomAnnotation("Tạo mã giảm giá thành công!")
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount){
        return ResponseEntity.ok(this.discountService.handleCreateDiscount(discount));
    }

    @GetMapping("/discounts/{id}")
    @CustomAnnotation("Thông tin chi tiết mã giảm giá!")
    public ResponseEntity<Discount> getDiscountById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.discountService.handleGetDiscountById(id));
    }

    @GetMapping("/discounts")
    @CustomAnnotation("Hiển thị danh sách thông tin mã giảm giá!")
    public ResponseEntity<ResPaginationDTO> getAllDiscounts(@Filter Specification<Discount> spec, Pageable pageable){
        return ResponseEntity.ok(this.discountService.handleGetDiscounts(spec, pageable));
    }

    @PutMapping("/discounts")
    @CustomAnnotation("Cập nhật mã giảm giá thành công!")
    public ResponseEntity<Discount> updateDiscount(@RequestBody ReqUpdateDiscount discount){
        return ResponseEntity.ok(this.discountService.handleUpdateDiscount(discount));
    }

    @DeleteMapping("/discounts/{id}")
    @CustomAnnotation("Xóa mã giảm giá thành công!")
    public ResponseEntity<Void> deleteDiscount(@PathVariable("id") Integer id){
        this.discountService.handleDeleteDiscountById(id);
        return ResponseEntity.ok(null);
    }
}
