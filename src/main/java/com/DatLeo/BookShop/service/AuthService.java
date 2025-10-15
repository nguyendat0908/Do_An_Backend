package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqChangePasswordDTO;
import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.dto.request.ReqRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResLoginDTO;
import com.DatLeo.BookShop.dto.response.ResRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;

public interface AuthService {

    ResLoginDTO handleLogin(ReqLoginDTO reqLoginDTO);
    ResLoginDTO.UserLogin handleGetAccount();
    ResLoginDTO getRefreshToken(String refreshToken);
    void handleLogout();
    ResRegisterDTO handleRegister(ReqRegisterDTO reqRegisterDTO);
    ResUserDTO activeAccountRegister(String username, String email, String code);
    ResRegisterDTO handleForgotPassword(String email);
    ResRegisterDTO handleChangePassword(ReqChangePasswordDTO reqChangePasswordDTO);
}
