package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqUpdateDiscount;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.entity.Discount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface DiscountService {

    Discount handleCreateDiscount(Discount discount);
    Discount handleUpdateDiscount(ReqUpdateDiscount discount);
    void handleDeleteDiscountById(Integer id);
    ResPaginationDTO handleGetDiscounts(Specification<Discount> spec, Pageable pageable);
    Discount handleGetDiscountById(Integer id);
}
