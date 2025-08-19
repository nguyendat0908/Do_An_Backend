package com.DatLeo.BookShop.exception;

public class ApiMessage {

    // User
    public static final String EMAIL_EXISTED = "Email người dùng đã tồn tại vui lòng chọn email khác.";
    public static final String ID_USER_NOT_EXIST = "Người dùng không tồn tại, vui lòng kiểm tra lại.";

    // Author
    public static final String NAME_EXISTED = "Tên tác giả đã tồn tại, vui lòng chọn tên khác.";
    public static final String AUTHOR_NOT_EXIST = "Tác giả không tồn tại, vui lòng kiểm tra lại.";

    // Filed message
    public static final String NAME_NOT_NULL = "Tên không được để trống.";
    public static final String EMAIL_NOT_NULL = "Email không được để trống.";
    public static final String EMAIL_NOT_CORRECT_FORMAT = "Email không đúng định dạng.";
    public static final String PASSWORD_NOT_NULL = "Mật khẩu không được để trống.";
    public static final String PASSWORD_MUST_BE_GREATER = "Mật khẩu phải lớn hơn 8 ký tự.";
    public static final String QUANTITY_NOT_NULL = "Số lượng không được để trống.";
    public static final String PRICE_NOT_NULL = "Giá không được để trống.";
    public static final String CODE_NOT_NULL = "Mã CODE không được để trống.";
    public static final String RECEIVER_NAME_NOT_NULL = "Tên người nhận hàng không được để trống.";
    public static final String RECEIVER_ADDRESS_NOT_NULL = "Địa chỉ người nhận hàng không được để trống.";
    public static final String RECEIVER_PHONE_NOT_NULL = "Số điện thoại người nhận hàng không được để trống.";
    public static final String PHONE_NUMBER_FORMAT = "Số điện thoại phải có 10 chữ số.";
}
