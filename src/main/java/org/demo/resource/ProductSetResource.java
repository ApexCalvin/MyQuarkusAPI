package org.demo.resource;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.demo.exception.UnprocessableEntityException;
import org.demo.model.EnglishSet;
import org.demo.service.ProductSetService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/set")
@Tag(name = "Set", description = "Operations for managing Product Set")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductSetResource {

    @Inject
    ProductSetService setService;

    @GET
    @Operation(summary = "Get all sets", description = "Retrieves all sets")
    @APIResponse(responseCode = "200", description = "Sets retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = EnglishSet.class, type = SchemaType.ARRAY)))
    public Response getAllSets() {
        List<EnglishSet> models = setService.getAllSets();
        return Response.ok(models).build();
    }

    @GET
    @Path("/{code}")
    @Operation(summary = "Get set by set code", description = "Retrieves set with the specified identifier")
    @APIResponse(responseCode = "200", description = "Set retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = EnglishSet.class)))
    @APIResponse(responseCode = "404", description = "Set not found")
    public Response getSet(@PathParam("code") @Parameter(description = "Set Identifier", example = "MEW")
            @NotEmpty(message = "Set code is required") String code) {
        EnglishSet model = setService.getSetByCode(code);
        return Response.ok(model).build();
    }
    
    @POST
    @Operation(summary = "Create a new Set", description = "Creates a new set with the provided details")
    @APIResponse(responseCode = "201", description = "Set created successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = EnglishSet.class)))
    public Response createSet(@Valid @RequestBody(description = "Set to create") EnglishSet request) {
        EnglishSet created = setService.createSet(request);
        return Response.created(URI.create("/v1/set/%s".formatted(created.getCode()))).entity(created).build();
    }

    @DELETE
    @Path("/{code}")
    @Operation(summary = "Delete a set", description = "Deletes the set with the specified identifier")
    @APIResponse(responseCode = "204", description = "Set deleted successfully")
    @APIResponse(responseCode = "404", description = "Set not found")
    public Response deleteSet(@PathParam("code") @Parameter(description = "Set Identifier", example = "MEW")
            @NotEmpty(message = "Code is required") String code) {
        setService.deleteSet(code);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{code}")
    @Operation(summary = "Update an existing set", description = "Updates the set with the specified identifier")
    @APIResponse(responseCode = "200", description = "Set updated successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = EnglishSet.class)))
    @APIResponse(responseCode = "404", description = "Set not found")
    @APIResponse(responseCode = "422", description = "Set code in the path must match the code in the request body")
    public Response updateSet(@PathParam("code") @Parameter(description = "Set Identifier", example = "MEW")
            @NotEmpty(message = "Code is required") String code, @Valid @RequestBody(description = "Set to update") EnglishSet request) {
        if (!StringUtils.equalsIgnoreCase(code, request.getName())) throw new UnprocessableEntityException("Set code in the path must match the code in the request body.");

        EnglishSet updated = setService.updateSet(code, request);
        return Response.ok(updated).build();
    }
}
