package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqUserAddress;
import com.DatLeo.BookShop.dto.response.ResUserAddress;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.entity.UserAddress;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.UserAddressRepository;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.UserAddressService;
import com.DatLeo.BookShop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final SecurityUtil securityUtil;
    private final UserRepository  userRepository;

    @Override
    public ResUserAddress handleCreateAddress(ReqUserAddress reqUserAddress) {

        Integer userId = securityUtil.getIdCurrentUserLogin();
        User user = userRepository.findById(userId).orElse(null);

        UserAddress userAddress = new UserAddress();
        userAddress.setReceiveName(reqUserAddress.getReceiveName());
        userAddress.setReceivePhone(reqUserAddress.getReceivePhone());
        userAddress.setDetailAddress(reqUserAddress.getDetailAddress());
        userAddress.setUser(user);
        userAddress.setWard(reqUserAddress.getWard());
        userAddress.setProvince(reqUserAddress.getProvince());
        userAddress.setDistrict(reqUserAddress.getDistrict());
        userAddress.setIsDefault(reqUserAddress.getIsDefault());
        userAddressRepository.save(userAddress);

        return convert(userAddress);
    }

    @Override
    public ResUserAddress handleUpdateAddress(ReqUserAddress reqUserAddress) {
        UserAddress userAddress = userAddressRepository.findById(reqUserAddress.getId())
                .orElseThrow(() -> new ApiException(ApiMessage.NOT_USER_ADDRESS));
        if (userAddress != null) {
            userAddress.setProvince(reqUserAddress.getProvince());
            userAddress.setWard(reqUserAddress.getWard());
            userAddress.setDistrict(reqUserAddress.getDistrict());
            userAddress.setDetailAddress(reqUserAddress.getDetailAddress());
            userAddress.setIsDefault(reqUserAddress.getIsDefault());
            userAddressRepository.save(userAddress);
        }

        return convert(userAddress);
    }

    @Override
    public List<ResUserAddress> handleGetAddresses() {
        Integer userId = securityUtil.getIdCurrentUserLogin();
        List<UserAddress> userAddresses = userAddressRepository.findAllByUserId(userId);
        List<ResUserAddress> resUserAddresses = userAddresses.stream().map(item -> convert(item)).toList();
        return resUserAddresses;
    }

    @Override
    public void handleDeleteAddress(Integer id) {
        userAddressRepository.deleteById(id);
    }

    private ResUserAddress convert(UserAddress userAddress) {
        ResUserAddress resUserAddress = new ResUserAddress();
        resUserAddress.setId(userAddress.getId());
        resUserAddress.setReceiveName(userAddress.getReceiveName());
        resUserAddress.setReceivePhone(userAddress.getReceivePhone());
        resUserAddress.setDetailAddress(userAddress.getDetailAddress());
        resUserAddress.setProvince(userAddress.getProvince());
        resUserAddress.setDistrict(userAddress.getDistrict());
        resUserAddress.setIsDefault(userAddress.getIsDefault());
        resUserAddress.setWard(userAddress.getWard());
        return resUserAddress;
    }
}
