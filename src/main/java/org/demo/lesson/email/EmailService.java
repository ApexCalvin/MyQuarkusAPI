package org.demo.lesson.email;

import java.util.List;
import java.util.Map;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {
    
    /** Injected Quarkus Mailer instance for sending emails. */
    @Inject
    Mailer mailer;

    public void generateEmail(String email) {
    sendEmail(org.demo.lesson.email.EmailNotificationRequest.builder()
            .recipients(List.of(email))
            .subject("Test Email")
            .content("This is a test email sent from Quarkus.")
            .build());
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

        // TODO (mail): successful sends is not viewable in Mailpit UI, error 500 5.5.2 Syntax error, command unrecognized
    }
}
