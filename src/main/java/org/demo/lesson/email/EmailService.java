package org.demo.lesson.email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@JBossLog
public class EmailService {
    
    /** Injected Quarkus Mailer instance for sending emails. */
    @Inject
    Mailer mailer;

    /** Qute template for rendering html body emails. */
    @Inject
    @Location("email-alert.html")
    Template emailAlert;

    public void generateRawTextEmail(String email) {
        sendEmail(org.demo.lesson.email.EmailNotificationRequest.builder()
                .recipients(List.of(email))
                .subject("Test Email")
                .content("This is a test email sent from Quarkus.")
                .build());
    }

    public void generateHtmlBodyEmail(String email, String text) {
        String htmlContent = renderEmailAlert(text);
        sendEmail(org.demo.lesson.email.EmailNotificationRequest.builder()
                .recipients(List.of(email))
                .subject("Test Email")
                .content(htmlContent)
                .html(true)
                .build());
    }

    private String renderEmailAlert(String text) {
        log.debugf("Rendering email template");
        // TODO (qute): edit CSS
        TemplateInstance instance = emailAlert
                .data("text", text)
                .data("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        String htmlBody = instance.render();
        log.debugf("Email template rendered successfully (%d characters)", htmlBody.length());

        return htmlBody; 
    }

    public void sendEmail(EmailNotificationRequest request) {
        
        Mail mail = new Mail();

        // set required fields
        mail.setTo(request.getRecipients());
        mail.setSubject(request.getSubject());
        // set content to either html or raw text
        if (request.isHtml()) {
            mail.setHtml(request.getContent());
        } else {
            mail.setText(request.getContent());
        }

        // optional headers
        Map<String, List<String>> headers = request.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            mail.setHeaders(headers);
        }

        // optional attachments 
        // TODO (mail): fix attachment handling
        // List<Attachment> att_str = request.getAttachments();
        // if (att_str != null && !att_str.isEmpty()) {
        //     List<Attachment> att_obj = att_str.stream()
        //             .map(att -> {
        //                 return new Attachment(att.getFilename(), att.getMimeType(), att.getData());})
        //             .collect(Collectors.toList());
        //
        //     if (CollectionUtils.isNotEmpty(att_obj)) {
        //         mail.setAttachments(att_obj);
        //     }
        // }

        // TODO (mail): invalid email recieves warnings under quarkus mailer but doesn't throw an exception? 

        mailer.send(mail);

        // TODO (mail): Test & remove mailpit app.props
    }
}
