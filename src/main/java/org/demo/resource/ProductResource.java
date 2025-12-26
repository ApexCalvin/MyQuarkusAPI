package org.demo.resource;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.demo.exception.UnprocessableEntityException;
import org.demo.model.Collectable;
import org.demo.service.ProductService;
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

@Path("/v1/product")
@Tag(name = "Product", description = "Operations for managing Product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    
    @Inject
    ProductService productService;

    @GET
    @Operation(summary = "Get all products", description = "Retrieves all products")
    @APIResponse(responseCode = "200", description = "Products retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = Collectable.class, type = SchemaType.ARRAY)))
    public Response getProducts() {
        List<Collectable> models = productService.getProducts();
        return Response.ok(models).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves product with the specified identifier")
    @APIResponse(responseCode = "200", description = "Product retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = Collectable.class)))
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response getProduct(@PathParam("id") @Parameter(description = "Product Identifier", example = "abc-123-def-456")
            @NotEmpty(message = "ID is required") String id) {
        Collectable model = productService.getProduct(id);
        return Response.ok(model).build();
    }
    
    @POST
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @APIResponse(responseCode = "201", description = "Product created successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = Collectable.class)))
    @APIResponse(responseCode = "422", description = "Product ID should be empty for new products")
    public Response createProduct(@Valid @RequestBody(description = "Product to create") Collectable request) {
        if (request.getId() != null) throw new UnprocessableEntityException("ID should not be provided when creating a new product.");
        
        Collectable created = productService.createProduct(request);
        return Response.created(URI.create("/v1/product/%s".formatted(created.getId()))).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes the product with the specified identifier")
    @APIResponse(responseCode = "204", description = "Product deleted successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response deleteProduct(@PathParam("id") @Parameter(description = "Product Identifier", example = "abc-123-def-456")
            @NotEmpty(message = "ID is required") String id) {
        productService.deleteProduct(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates the product with the specified identifier")
    @APIResponse(responseCode = "200", description = "Product updated successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = Collectable.class)))
    @APIResponse(responseCode = "404", description = "Product not found")
    @APIResponse(responseCode = "422", description = "Product ID in the path must match the ID in the request body")
    public Response updateProduct(@PathParam("id") @Parameter(description = "Product Identifier", example = "abc-123-def-456")
            @NotEmpty(message = "ID is required") String id, @Valid @RequestBody(description = "Product to update") Collectable request) {
        if (!StringUtils.equalsIgnoreCase(id, request.getId())) throw new UnprocessableEntityException("Product ID in the path must match the ID in the request body.");
        
        Collectable updated = productService.updateProduct(id, request);
        return Response.ok(updated).build();
    }
}
