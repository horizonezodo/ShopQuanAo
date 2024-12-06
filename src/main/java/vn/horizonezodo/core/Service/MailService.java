package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String to;

    @Value("${app.client.url}")
    private String portUrl;

    @Value("${server.url}")
    private String backendUrl;

    @Autowired
    private JavaMailSender mailSender;

    public void SendMail(String mail,String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(to);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("Verification Email");
        String htmlContent = readHtmlEmailTemplate();
        htmlContent = htmlContent.replace("{{var_href}}", portUrl+"/auth/verifiedSuccess?email="+mail + "&token="+token);
        message.setContent(htmlContent,"text/html; charset=utf-8");
        //log.info("send email success : " );
        mailSender.send(message);
    }

    public String readHtmlEmailTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("email-template.html");
            byte[] fileData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            //log.info("read template success : " );
            return new String(fileData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            //log.error("read template failure : " );
            e.printStackTrace();
            // Handle the exception appropriately
            return null;
        }
    }

    public void SendMailVerifyed(String mail,String token,String phone) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(to);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("Verification Email");
        String htmlContent = readHtmlEmailTemplate();
        htmlContent = htmlContent.replace("{{var_href}}", backendUrl+"/auth/verifiedSuccess?email="+mail + "&token="+token + "&phone="+phone);
        message.setContent(htmlContent,"text/html; charset=utf-8");
        //log.info("send email success : " );
        mailSender.send(message);
    }
}
