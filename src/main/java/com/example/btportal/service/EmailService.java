package com.example.btportal.service;

import com.example.btportal.model.FileDocument;
import com.example.btportal.model.PostApplication;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApplicationEmailWithCV(PostApplication app) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String jobTitle = app.getGeneratePostApplication() != null
                    ? app.getGeneratePostApplication().getTitle()
                    : "Unknown Job";

            helper.setTo("Mhlangathamy@gmail.com"); // 🔁 replace with HR email
            helper.setSubject("New Application for: " + jobTitle);

            String body = String.format(
                    "A new applicant has applied for the position of \"%s\".\n\n" +
                            "Applicant Details:\n" +
                            "- Full Name: %s %s\n" +
                            "- Email: %s\n" +
                            "- Phone: %s\n" +
                            "- Gender: %s\n" +
                            "- Race: %s\n" +
                            "- Age: %d\n",
                    jobTitle,
                    app.getFullnames(),
                    app.getSurname(),
                    app.getEmail(),
                    app.getPhoneNumber(),
                    app.getGender(),
                    app.getRace(),
                    app.getAge()
            );

            helper.setText(body);

            // ✅ Attach first available file (CV) if exists
            List<FileDocument> files = app.getFiles();
            if (files != null && !files.isEmpty()) {
                for (FileDocument file : files) {
                    helper.addAttachment(file.getFileName(),
                            new org.springframework.core.io.ByteArrayResource(file.getData()));
                }
            }

            mailSender.send(message);

            System.out.println("✅ Email sent successfully to HR.");

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
