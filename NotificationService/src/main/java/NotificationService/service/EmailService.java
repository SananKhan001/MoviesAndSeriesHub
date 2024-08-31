package NotificationService.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final String SRC_PATH = "src/main/resources/static/notification.html";

    @Async
    public void sendEmail(String subject,String greeting, String body, String to) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);

            String htmlPage = readHTMLFile(SRC_PATH);
            htmlPage = modifyingHTMLContent(htmlPage, subject, greeting, body);

            message.setContent(htmlPage, "text/html; charset=utf-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        javaMailSender.send(message);
    }

    private String readHTMLFile(String filepath) throws IOException {
        Path path = Path.of(filepath);
        return new String(Files.readAllBytes(path));
    }

    private String modifyingHTMLContent(String htmlContent, String heading, String greeting, String message) {
        Pattern headingPattern = Pattern.compile("(<h1 id=\"heading\">)(.*?)(</h1>)", Pattern.DOTALL);
        Matcher headingMatcher = headingPattern.matcher(htmlContent);

        Pattern greetingPattern = Pattern.compile("(<h2 id=\"greeting\">)(.*?)(</h2>)", Pattern.DOTALL);
        Matcher greetingMatcher = greetingPattern.matcher(htmlContent);

        Pattern messagePattern = Pattern.compile("(<p id=\"message\">)(.*?)(</p>)", Pattern.DOTALL);
        Matcher messageMatcher = messagePattern.matcher(htmlContent);

        if(headingMatcher.find() && greetingMatcher.find() && messageMatcher.find()) {
            htmlContent = headingMatcher.replaceAll("<h1 id=\"heading\">" + heading + "</h1>")
                    .replaceAll("(<h2 id=\"greeting\">)(.*?)(</h2>)",
                            "<h2 id=\"greeting\">" + greeting + "</h2>")
                    .replaceAll("(<p id=\"message\">)(.*?)(</p>)",
                            "<p id=\"message\">" + message + "</p>");
        }

        return htmlContent;
    }

}
