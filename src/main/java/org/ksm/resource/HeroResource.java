package org.ksm.resource;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.ksm.model.HeroRequest;
import org.ksm.entity.HeroResponse;
import org.ksm.exception.UnprocessableEntityException;
import org.ksm.repository.HeroRepository;
import org.ksm.service.HeroService;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.jbosslog.JBossLog;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Path("/v1/hero")
@Tag(name = "Hero", description = "Operations for managing Hero")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HeroResource {
    
    @Inject
    HeroService heroService;

    @GET
    @Path("/load")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadDummyData(){
        heroService.loadDummyData();
        return Response.ok("Dummy heroes loaded").build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get hero by Id", description = "Retrieves hero with the specified identifier")
    //@CacheResult(cacheName = CacheService.CACHE_HEROES)
    @APIResponse(
        responseCode = "200",
        description = "Hero retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON, 
            schema = @Schema(implementation = HeroRequest.class)))
    @APIResponse(responseCode = "404", description = "Hero not found")
    public Response getHero(
            //@CacheKey
            @PathParam("id") 
            @Parameter(description = "Hero Identifier", example = "1234")
            @NotEmpty String id) {
        HeroRequest model = heroService.getHero(id);
        return Response.ok(model).build();
    }

    @GET
    @Operation(summary = "Get all heroes", description = "Retrieves all heroes")
    @APIResponse(
        responseCode = "200",
        description = "Heroes retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = HeroRequest.class, 
            type = SchemaType.ARRAY)))
    //@CacheResult(cacheName = CacheService.CACHE_HEROES)
    public Response getHeroes() {
        List<HeroRequest> models = heroService.getHeroes();
        return Response.ok(models).build();
    }

    @POST
    @Operation(summary = "Create new hero", description = "Creates a new hero")
    @APIResponse(
            responseCode = "201",
            description = "Hero created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = HeroRequest.class)))
    @APIResponse(responseCode = "422", description = "Invalid payload data")
    //@CacheInvalidate(cacheName = CacheService.CACHE_HERO)
    public Response createHero(
            @Valid 
            @RequestBody(description = "Hero to create") HeroRequest hero) {
        if (hero.getId() != null) throw new UnprocessableEntityException("Id should not be provided when creating a new hero.");
        
        HeroRequest created = heroService.createHero(hero);
        return Response.created(URI.create("/v1/hero/%s".formatted(created.getId())))
                .entity(created)
                .build();
    }


    // @DELETE
    // @Path("/{id}")
    // @Transactional
    // public Response deleteById(@PathParam("id") Long heroId) {
    //     if (heroRepository.deleteById(heroId)) {
    //         return Response.status(Status.OK).build();
    //     }
    //     return Response.status(Status.NOT_FOUND).build();
    // }


    // @PUT
    // @Path("/{id}")
    // @Transactional
    // public Response replaceById(@PathParam("id") Long heroId, @Valid Hero hero) {
    //     Hero exist = heroRepository.findById(heroId);
        
    //     if (exist == null) return Response.status(Status.NOT_FOUND).build();
        
    //     exist.setAlias(hero.getAlias());
    //     exist.setName(hero.getName());
    //     exist.setCanFly(hero.getCanFly());
        
    //     return Response.status(Status.OK).entity(exist).build();
    // }


    // @PATCH
    // @Path("/{id}")
    // @Transactional
    // public Response updateById(@PathParam("id") Long heroId, @Valid HeroRequest HeroRequest) {
    //     Hero exist = heroRepository.findById(heroId);

    //     if (exist == null) return Response.status(Status.NOT_FOUND).build();

    //     if (HeroRequest.getAlias() != null) exist.setAlias(HeroRequest.getAlias());
    //     if (HeroRequest.getName() != null) exist.setName(HeroRequest.getName());
    //     if (HeroRequest.getCanFly() != null) exist.setCanFly(HeroRequest.getCanFly());

    //     return Response.status(Status.OK).entity(exist).build();
    // }

}
