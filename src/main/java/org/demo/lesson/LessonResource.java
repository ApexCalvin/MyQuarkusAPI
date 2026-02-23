package org.demo.lesson;

import java.net.URI;

import org.demo.entity.BlobStorage;
import org.demo.lesson.email.EmailService;
import org.demo.service.BlobStorageService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
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

    @Inject
    BlobStorageService blobStorageService;

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

    @PUT
    @Path("/{id}/attachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(
            summary = "Upload attachment to entity",
            description = "Uploads a single new attachment such as a photo or a document to an entity")
    @APIResponse(responseCode = "201", description = "Attachment uploaded successfully")
    @APIResponse(responseCode = "400", description = "Invalid file format")
    @APIResponse(responseCode = "404", description = "Entity not found")
    @APIResponse(responseCode = "500", description = "Internal server error during upload")
    public Response uploadAttachment(
            @PathParam("id") @Parameter(description = "Entity ID", example = "b3b8c7e2-4e2a-4c1a-9e2b-1a2b3c4d5e6f", required = true) String id,
            @RestForm("file") FileUpload fileUploadRequest) {
        final BlobStorage storedFile = blobStorageService.uploadAttachment(id, fileUploadRequest);
        return Response.created(URI.create("/v1/entity/" + id + "/attachment/" + storedFile.getId().getBlobLine())).build();
    }

    @GET
    @Path("/{id}/attachment/{attachmentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Operation(
            summary = "Download attachment for entity",
            description = "Downloads an attachment such as a photo or a document for an entity")
    @APIResponse(
            responseCode = "200",
            description = "Attachment downloaded successfully",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM,
                            schema = @Schema(type = SchemaType.STRING, format = "binary")))
    @APIResponse(responseCode = "404", description = "Attachment not found")
    @APIResponse(responseCode = "500", description = "Internal server error during download")
    public Response downloadAttachment(
            @PathParam("id")
                    @Parameter(
                            description = "Entity ID",
                            example = "b3b8c7e2-4e2a-4c1a-9e2b-1a2b3c4d5e6f",
                            required = true)
                    String id,
            @PathParam("attachmentId") @Parameter(description = "Attachment ID", example = "1", required = true)
                    Long attachmentId) {
        final BlobStorage attachment = blobStorageService.getAttachment(id, attachmentId);
        final byte[] attachmentBytes = attachment.getBlobItem();
        
        final Response.ResponseBuilder response = Response.ok(attachmentBytes);
        response.header("Content-Type", attachment.getMimeType());
        response.header("Content-Disposition", "attachment; filename=\"" + attachment.getBlobName() + "\"");
        response.header("Access-Control-Expose-Headers", "Content-Disposition");
        return response.build();
    }
}
