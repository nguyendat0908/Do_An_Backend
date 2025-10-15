package com.DatLeo.BookShop.exception;

public class ApiMessage {

    // Auth
    public  static final String NOT_REFRESH_TOKEN_COOKIE = "Không có refresh_token ở cookie.";
    public static final String ERROR_REFRESH_TOKEN = "Refresh token không tồn tại hoặc không hợp lệ.";
    public static final String ERROR_OTP = "Mã OTP không hợp lệ hoặc đã hết hạn. Vui lòng kiểm tra lại.";
    public static final String USER_INACTIVE = "Người dùng chưa được kích hoạt tài khoản.";
    public static final String OLD_PASSWORD = "Mật khẩu không chính xác.";
    public static final String ERROR_CHANGE_PASSWORD = "Mật khẩu xác nhận không trùng khớp.";

    // User
    public static final String EMAIL_EXISTED = "Email người dùng đã tồn tại vui lòng chọn email khác.";
    public static final String ID_USER_NOT_EXIST = "Người dùng không tồn tại, vui lòng kiểm tra lại.";

    // Author
    public static final String AUTHOR_NAME_EXISTED = "Tên tác giả đã tồn tại, vui lòng chọn tên khác.";
    public static final String AUTHOR_NOT_EXIST = "Tác giả không tồn tại, vui lòng kiểm tra lại.";

    // Category
    public static final String CATEGORY_NAME_EXISTED = "Tên danh mục đã tồn tại, vui lòng chọn tên khác.";
    public static final String CATEGORY_NOT_EXIST = "Danh mục không tồn tại, vui lòng kiểm tra lại.";

    // Book
    public static final String BOOK_NAME_EXISTED = "Tên sách đã tồn tại, vui lòng chọn tên khác.";
    public static final String BOOK_NOT_EXIST = "Sách không tồn tại, vui lòng kiểm tra lại.";

    // Discount
    public static final String DISCOUNT_CODE_EXISTED = "Tên mã giảm giá đã tồn tại, vui lòng chọn tên khác.";
    public static final String DISCOUNT_NOT_EXIST = "Mã giảm giá không tồn tại, vui lòng kiểm tra lại.";
    public static final String DISCOUNT_COUNT_NOT_NULL = "Số lượng mã không được để trống.";
    public static final String DISCOUNT_END_DATE = "Ngày kết thúc không được để trống.";
    public static final String DISCOUNT_START_DATE = "Ngày bắt đầu không được để trống.";
    public static final String DISCOUNT_END_DATE_ERROR = "Ngày kết thúc phải sau ngày kết thúc hiện tại.";

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
    public static final String AUTHOR_NOT_NULL = "Tác giả không được để trống.";
    public static final String CATEGORY_NOT_NULL = "Danh mục không được để trống.";

    // Upload file
    public static final String FILE_EMPTY = "File tải lên trống.";
    public static final String ERROR_FILE_MIMETYPE = "File không đúng định dạng cho phép (jpeg, png, jpg).";
    public static final String ERROR_FILE_SIZE = "File quá kích thước cho phép <2MB.";
}
