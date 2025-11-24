package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ShippingFeeRequest;
import com.DatLeo.BookShop.service.ShippingService;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces() {
        return ResponseEntity.ok(shippingService.getProvinces());
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistricts(@RequestParam("provinceId") Integer provinceId) {
        return ResponseEntity.ok(shippingService.getDistricts(provinceId));
    }

    @GetMapping("/wards")
    public ResponseEntity<?> getWards(@RequestParam("districtId") Integer districtId) {
        return ResponseEntity.ok(shippingService.getWards(districtId));
    }

    @PostMapping("/fee-ship-calculate")
    public ResponseEntity<?> calculateFee(@RequestBody ShippingFeeRequest req) {
        return ResponseEntity.ok(shippingService.calculateShippingFee(req));
    }
}
