package com.grash.service;

import com.grash.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import static com.grash.utils.Consts.FRONT_TUTORIAL_LINK;

@Service
@Transactional
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Utils utils;

    @Async
    public void send(String to, String objet, String email) {
        try {
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject(objet);
            mailSender.send(mimeMessage);
        } catch ( Exception e) {
            throw new IllegalStateException("Failed to send email !");
        }
    }

    public String buildEmail(String objet, String title, String link) {
        return utils.readFile("src/main/java/campus/utils/emailVerification1.html")+ "\""+
                link +"\""+
                utils.readFile("src/main/java/campus/utils/emailVerification2.html")+"\""+
                FRONT_TUTORIAL_LINK +"\""+
                utils.readFile("src/main/java/campus/utils/emailVerification3.html");
    }
}
