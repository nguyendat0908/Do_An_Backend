package com.DatLeo.BookShop.service;

public interface EmailService {

    void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmailActiveAccount(String to, String subject, String templateName, String code);
    void sendEmailNewPassword(String to, String subject, String templateName, String newPassword);
}
