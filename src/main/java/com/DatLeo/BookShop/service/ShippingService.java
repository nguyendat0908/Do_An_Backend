package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ShippingFeeRequest;
import com.DatLeo.BookShop.dto.response.ShippingFeeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.shop-id}")
    private Integer shopId;

    @Value("${ghn.from-district}")
    private Integer districtId;

    @Value("${ghn.from-ward-code}")
    private String wardCode;

    @Value("${ghn.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    // Hàm chuẩn bị headers GHN
    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", String.valueOf(shopId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public JsonNode getProvinces() {
        String url = baseUrl + "/master-data/province";
        HttpEntity<?> entity = new HttpEntity<>(headers());
        ResponseEntity<JsonNode> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        return response.getBody().get("data");
    }

    public JsonNode getDistricts(Integer provinceId) {
        String url = baseUrl + "/master-data/district?province_id=" + provinceId;
        HttpEntity<?> entity = new HttpEntity<>(headers());
        ResponseEntity<JsonNode> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        return response.getBody().get("data");
    }

    public JsonNode getWards(Integer districtId) {
        String url = baseUrl + "/master-data/ward?district_id=" + districtId;
        HttpEntity<?> entity = new HttpEntity<>(headers());
        ResponseEntity<JsonNode> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        return response.getBody().get("data");
    }

    public ShippingFeeResponse calculateShippingFee(ShippingFeeRequest req) {
        String url = baseUrl + "/v2/shipping-order/fee";

        Map<String, Object> body = new HashMap<>();
        body.put("from_district_id", districtId);
        body.put("from_ward_code", wardCode);
        body.put("service_type_id", 2);
        body.put("to_district_id", req.getToDistrictId());
        body.put("to_ward_code", req.getToWardCode());
        body.put("height", 10);
        body.put("length", 15);
        body.put("width", 10);
        body.put("weight", req.getWeight());

        HttpEntity<?> entity = new HttpEntity<>(body, headers());
        ResponseEntity<Object> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);

        return new ShippingFeeResponse(
                req.getReceiverName(),
                req.getReceiverPhone(),
                req.getReceiverAddress(),
                response.getBody()
        );
    }
}
