package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqUserAddress;
import com.DatLeo.BookShop.dto.response.ResUserAddress;
import com.DatLeo.BookShop.entity.UserAddress;

import java.util.List;

public interface UserAddressService {

    ResUserAddress handleCreateAddress(ReqUserAddress reqUserAddress);
    ResUserAddress handleUpdateAddress(ReqUserAddress reqUserAddress);
    List<ResUserAddress> handleGetAddresses();
    void handleDeleteAddress(Integer id);
}
