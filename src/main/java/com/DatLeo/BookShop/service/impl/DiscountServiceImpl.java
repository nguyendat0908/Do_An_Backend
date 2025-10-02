package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqUpdateDiscount;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Discount;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.DiscountRepository;
import com.DatLeo.BookShop.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount handleCreateDiscount(Discount discount) {
        boolean isCheckDiscountExist = this.discountRepository.existsByCode(discount.getCode());
        if (isCheckDiscountExist) {
            log.error("Tạo mã giảm giá không thành công {}", ApiMessage.DISCOUNT_CODE_EXISTED);
            throw new ApiException(ApiMessage.DISCOUNT_CODE_EXISTED);
        }
        return this.discountRepository.save(discount);
    }

    @Override
    public Discount handleGetDiscountById(Integer id) {
        Optional<Discount> discountOptional = this.discountRepository.findById(id);
        if (discountOptional == null) {
            log.error("Mã giảm giá không tồn tại với ID = {}", id);
            throw new ApiException(ApiMessage.DISCOUNT_NOT_EXIST);
        }
        return discountOptional.get();
    }

    @Override
    public Discount handleUpdateDiscount(ReqUpdateDiscount discount) {
        Discount currentDiscount = this.handleGetDiscountById(discount.getId());
        currentDiscount.setUsageLimit(discount.getUsageLimit());
        currentDiscount.setEndDate(discount.getEndDate());
        log.info("Cập nhật mã giảm giá thành công với ID = {}", discount.getId());
        return this.discountRepository.save(currentDiscount);
    }

    @Override
    public void handleDeleteDiscountById(Integer id) {
        Discount discount = this.handleGetDiscountById(id);
        this.discountRepository.deleteById(discount.getId());
        log.info("Xóa mã giảm giá thành công với ID = {}", id);
    }

    @Override
    public ResPaginationDTO handleGetDiscounts(Specification<Discount> spec, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Discount> pageDiscount = this.discountRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageDiscount.getTotalPages());
        meta.setTotal(pageDiscount.getTotalElements());

        resPaginationDTO.setMeta(meta);
        List<Discount> listDiscount = pageDiscount.getContent();
        resPaginationDTO.setResult(listDiscount);

        return resPaginationDTO;
    }
}
