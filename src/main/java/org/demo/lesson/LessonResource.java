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
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sendEmail")
    @Operation(summary = "Send Email", description = "using Quarkus Mailer extension")
    public Response sendTestEmail(@QueryParam("Email") @Parameter(description = "The recipient email address", example = "example@noreply.com") @NotBlank String email) {
        emailService.generateEmail(email);
        return Response.ok().build();
    }
}
