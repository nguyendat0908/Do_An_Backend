package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqDiscountDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateDiscount;
import com.DatLeo.BookShop.dto.response.ResDiscountDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.entity.Category;
import com.DatLeo.BookShop.entity.Discount;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.CategoryRepository;
import com.DatLeo.BookShop.repository.DiscountRepository;
import com.DatLeo.BookShop.service.DiscountService;
import com.DatLeo.BookShop.util.constant.ApplyDiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ResDiscountDTO handleCreateDiscount(ReqDiscountDTO reqDiscountDTO) {
        boolean isCheckDiscountExist = this.discountRepository.existsByCode(reqDiscountDTO.getCode());
        if (isCheckDiscountExist) {
            log.error("Tạo mã giảm giá không thành công {}", ApiMessage.DISCOUNT_CODE_EXISTED);
            throw new ApiException(ApiMessage.DISCOUNT_CODE_EXISTED);
        }

        Discount discount = buildDiscount(reqDiscountDTO);
        discountRepository.save(discount);

        return convertToDTO(discount);
    }

    private Discount buildDiscount(ReqDiscountDTO reqDiscountDTO) {
        Discount discount = new Discount();
        discount.setCode(reqDiscountDTO.getCode());
        discount.setType(reqDiscountDTO.getType());
        discount.setApply(reqDiscountDTO.getApply());
        discount.setValueCash(reqDiscountDTO.getValueCash());
        discount.setValuePercent(reqDiscountDTO.getValuePercent());
        discount.setActive(true);
        discount.setMinValue(reqDiscountDTO.getMinValue());
        discount.setStartDate(reqDiscountDTO.getStartDate());
        discount.setEndDate(reqDiscountDTO.getEndDate());
        discount.setUsageLimit(reqDiscountDTO.getUsageLimit());

        if (reqDiscountDTO.getApply() == ApplyDiscountType.PRODUCT_CATEGORY && reqDiscountDTO.getCategoryIds() != null
        && !reqDiscountDTO.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(reqDiscountDTO.getCategoryIds()));
            discount.setCategories(categories);
        }

        return discount;
    }

    @Override
    @Transactional(readOnly = true)
    public ResDiscountDTO handleGetDiscountById(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiMessage.DISCOUNT_NOT_EXIST));

        if (discount.getApply() == ApplyDiscountType.PRODUCT_CATEGORY) {
            discount = discountRepository.findByIdWithCategories(id)
                    .orElseThrow(() -> new ApiException(ApiMessage.DISCOUNT_NOT_EXIST));
        }

        return convertToDTO(discount);
    }

    @Override
    public List<ResDiscountDTO> handleGetDiscountTypeFreeShipping() {
        List<Discount> discounts = discountRepository.findFreeShippingDiscounts();
        List<ResDiscountDTO> resDiscountDTOS = discounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return resDiscountDTOS;
    }

    @Override
    public List<ResDiscountDTO> handleGetDiscountTypeCash() {
        List<Discount> discounts = discountRepository.findCashAndPercentDiscounts();
        List<ResDiscountDTO> resDiscountDTOS = discounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return resDiscountDTOS;
    }

    @Override
    @Transactional
    public ResDiscountDTO handleUpdateDiscount(ReqUpdateDiscount discount) {
        Discount currentDiscount = discountRepository.findById(discount.getId())
                .orElseThrow(() -> new ApiException(ApiMessage.DISCOUNT_NOT_EXIST));

        if (Boolean.FALSE.equals(discount.getActive())) {
            if (currentDiscount.getEndDate().isAfter(LocalDate.now())) {
                if (currentDiscount.getOrders() != null && !currentDiscount.getOrders().isEmpty()) {
                    throw new ApiException(ApiMessage.DISCOUNT_ALREADY_USED);
                }
            }
            currentDiscount.setUsageLimit(0);
        } else {
            Integer currentCount = currentDiscount.getUsageLimit();
            currentDiscount.setUsageLimit(currentCount + discount.getUsageLimit());
        }

        if (discount.getEndDate().isBefore(currentDiscount.getEndDate())) {
            throw new ApiException(ApiMessage.DISCOUNT_END_DATE_ERROR);
        }

        if (discount.getEndDate().isBefore(LocalDate.now())) {
            throw new ApiException("Ngày kết thúc không thể ở quá khứ.");
        }

        currentDiscount.setEndDate(discount.getEndDate());
        currentDiscount.setActive(discount.getActive());

        discountRepository.save(currentDiscount);

        log.info("Cập nhật mã giảm giá thành công (ID = {}, code = {})",
                discount.getId(), currentDiscount.getCode());

        return convertToDTO(currentDiscount);
    }

    @Override
    public void handleDeleteDiscountById(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiMessage.DISCOUNT_NOT_EXIST));
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
        List<ResDiscountDTO> resDiscountDTO = listDiscount.stream().map(item -> convertToDTO(item)).toList();
        resPaginationDTO.setResult(resDiscountDTO);

        return resPaginationDTO;
    }

    private ResDiscountDTO convertToDTO(Discount discount) {
        Set<ResDiscountDTO.CategoryDTO> categoryDTOs = null;

        if (discount.getApply() == ApplyDiscountType.PRODUCT_CATEGORY && discount.getCategories() != null) {
            categoryDTOs = discount.getCategories().stream()
                    .map(c -> new ResDiscountDTO.CategoryDTO(c.getId(), c.getName()))
                    .collect(Collectors.toSet());
        }

        if (discount.getEndDate().isBefore(LocalDate.now())) {
            discount.setActive(false);
        }

        return ResDiscountDTO.builder()
                .id(discount.getId())
                .code(discount.getCode())
                .type(discount.getType())
                .apply(discount.getApply())
                .value(
                        Optional.ofNullable(discount.getValueCash())
                                .orElseGet(() -> Optional.ofNullable(discount.getValuePercent())
                                        .map(Double::valueOf)
                                        .orElse(null))
                )
                .active(discount.getActive())
                .minValue(discount.getMinValue())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .usageLimit(discount.getUsageLimit())
                .usedCount(discount.getUsedCount())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .categories(categoryDTOs)
                .build();
    }
}
