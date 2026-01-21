package org.demo.resource;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.demo.exception.UnprocessableEntityException;
import org.demo.model.EnglishSet;
import org.demo.model.SealedProductType;
import org.demo.service.ProductTypeService;
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

@Path("/v1/product-type")
@Tag(name = "Product Type", description = "Operations for managing Product Types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTypeResource {
    
    @Inject
    ProductTypeService productTypeService;

    @GET
    @Operation(summary = "Get all product types", description = "Retrieves all product types")
    @APIResponse(responseCode = "200", description = "Product types retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = SealedProductType.class, type = SchemaType.ARRAY)))
    public Response getAllProductTypes() {
        List<SealedProductType> models = productTypeService.getAllProductTypes();
        return Response.ok(models).build();
    }

    @GET
    @Path("/{name}")
    @Operation(summary = "Get product type by name", description = "Retrieves product type with the specified name")
    @APIResponse(responseCode = "200", description = "Product type retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = SealedProductType.class)))
    @APIResponse(responseCode = "404", description = "Product type not found")
    public Response getProductType(@PathParam("name") @Parameter(description = "Product Type Name", example = "Booster Box") String name) {
        SealedProductType model = productTypeService.getProductTypeById(name);
        return Response.ok(model).build();
    }
    
    @POST
    @Operation(summary = "Create a product type", description = "Creates a new product type with the provided details")
    @APIResponse(responseCode = "201", description = "Product type created successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = SealedProductType.class)))
    public Response createProductType(@Valid @RequestBody(description = "Product type to create") SealedProductType request) {
        SealedProductType created = productTypeService.createProductType(request);
        return Response.created(URI.create("/v1/product-type/%s".formatted(created.getName()))).entity(created).build();
    }

    @DELETE
    @Path("/{name}")
    @Operation(summary = "Delete a product type", description = "Deletes the product type with the specified identifier")
    @APIResponse(responseCode = "204", description = "Product type deleted successfully")
    @APIResponse(responseCode = "404", description = "Product type not found")
    public Response deleteProductType(@PathParam("name") @Parameter(description = "Product Type Name", example = "Booster Box")
            @NotEmpty(message = "Name is required") String name) {
        productTypeService.deleteProductType(name);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{name}")
    @Operation(summary = "Update an existing product type", description = "Updates the product type with the specified identifier")
    @APIResponse(responseCode = "200", description = "Product type updated successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = EnglishSet.class)))
    @APIResponse(responseCode = "404", description = "Product type not found")
    @APIResponse(responseCode = "422", description = "Product type name in the path must match the name in the request body")
    public Response updateSet(@PathParam("name") @Parameter(description = "Product Type Name", example = "Booster Box")
            @NotEmpty(message = "Name is required") String name, @Valid @RequestBody(description = "Product type to update") SealedProductType request) {
        if (!StringUtils.equalsIgnoreCase(name, request.getName())) throw new UnprocessableEntityException("Product type name in the path must match the name in the request body.");
        
        SealedProductType updated = productTypeService.updateProductType(name, request);
        return Response.ok(updated).build();
    }
}