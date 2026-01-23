package org.demo.lesson;

import org.demo.lesson.email.EmailService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/lesson")
@Tag(name = "Lessons", description = "Lessons learned from various exercises")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LessonResource {

    @Inject
    EmailService emailService;

    @GET
    @Path("/sendEmail")
    @Operation(summary = "Send Email", description = "using Quarkus Mailer extension")
    public Response sendTestEmail(@QueryParam("Email") @Parameter(description = "The recipient email address", example = "example@noreply.com") @NotBlank String email) {
        emailService.generateRawTextEmail(email);
        return Response.ok().build();
    }

    @GET
    @Path("/sendHtmlEmail")
    @Operation(summary = "Send Email with HTML Body", description = "using Quarkus Mailer extension and Qute template to render HTML body")
    public Response sendTestHtmlEmail(
          @QueryParam("Email") @Parameter(description = "The recipient email address", example = "example@noreply.com") @NotBlank String email, 
          @QueryParam("Text") @Parameter(description = "Text to be displayed in the email", example = "Hello World!") @NotBlank String text) {
        emailService.generateHtmlBodyEmail(email, text);
        return Response.ok().build();
    }
}
