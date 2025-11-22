package org.ksm.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.ksm.model.Collectable;
import org.ksm.service.ProductService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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
    @APIResponse(
        responseCode = "200",
        description = "Products retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Collectable.class, 
            type = SchemaType.ARRAY)))
    public Response getProducts() {
        List<Collectable> models = productService.getProducts();
        return Response.ok(models).build();
    }
    
}
