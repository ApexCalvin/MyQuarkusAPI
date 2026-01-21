package org.demo.lesson.email;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Email notification request object used to send emails.
 *
 * <p>This class represents a complete email notification request that can be used with Quarkus Mailer to send emails.
 * It supports all standard email features including recipients, subject, content, attachments, headers, and various
 * email options.
 *
 * <p>The class is designed to be immutable and uses builder pattern for easy construction. All required fields are
 * validated using Jakarta Validation annotations to ensure data integrity before email sending.
 *
 * <p>Supports the Quarkus Mailer send method parameters and can be easily serialized to/from JSON using Jackson
 * annotations.
 *
 * @see EmailService
 */
@Getter
@Builder
@ToString
@Jacksonized
public class EmailNotificationRequest {

    @NotEmpty 
    private final List<@Email String> recipients;

    @NotBlank 
    private final String subject;

    @NotBlank 
    private final String content;

    @Singular
    private final List<@Valid Attachment> attachments;

    @Singular
    private final Map<String, List<String>> headers;
    //TODO (mail): what does @Email validate for then?
    @Singular
    private final List<@Email String> bccs;

    @Singular
    private final List<@Email String> ccs;

    private final String bounceAddress;
    private final String replyTo;
    private final boolean html;

    @Builder
    @Getter
    @ToString
    @Jacksonized
    public static class Attachment {

        @NotBlank 
        private final String filename;

        @NotBlank 
        private final String mimeType;

        @NotEmpty 
        private final byte[] data;
    }
}