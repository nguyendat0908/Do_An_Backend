package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("Lỗi khi gửi email: " + e);
        }
    }

    @Override
    public void sendEmailActiveAccount(String to, String subject, String templateName, String code) {
        Context context = new Context();
        context.setVariable("code", code);

        String content = templateEngine.process(templateName, context);
        sendEmailSync(to, subject, content, false, true);
    }

    @Override
    public void sendEmailNewPassword(String to, String subject, String templateName, String newPassword) {
        Context context = new Context();
        context.setVariable("newPassword", newPassword);

        String content = templateEngine.process(templateName, context);
        sendEmailSync(to, subject, content, false, true);
    }
}
