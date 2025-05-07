package org.ksm;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.ksm.dto.HeroRequest;

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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.validation.Valid;


@Path("/hero")
public class HeroResource {
    
    @Inject
    HeroRepository heroRepository;

    @Consumes("application/json")
    @Produces("application/json")
    @GET
    @Path("/load")
    @Transactional
    public void load(){
        new Hero("batman", "bruce", false).persist(); 
        new Hero("ironman", "tony", true).persist();
    }

    @Consumes("application/json")
    @Produces("application/json")       
    @GET
    @Operation(summary = "test summary for getAll", description = "test description for getAll")
    public List<Hero> getAll() {
         return heroRepository.listAll();
    }

    @Consumes("application/json")
    @Produces("application/json")
    @GET
    @Path("/{id}")
    public Hero getById(@PathParam("id") Long heroId) {
        return heroRepository.findById(heroId);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @Transactional
    public Response create(@Valid Hero hero) {
        hero.id = null;
        hero.persist();
        return Response.status(Status.CREATED).entity(hero).build();
    }

    @Consumes("application/json")
    @Produces("application/json")
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long heroId) {
        if (heroRepository.deleteById(heroId)) {
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    @Path("/{id}")
    @Transactional
    public Response replaceById(@PathParam("id") Long heroId, @Valid Hero hero) {
        Hero exist = heroRepository.findById(heroId);
        
        if (exist == null) return Response.status(Status.NOT_FOUND).build();
        
        exist.setAlias(hero.getAlias());
        exist.setName(hero.getName());
        exist.setCanFly(hero.getCanFly());
        
        return Response.status(Status.OK).entity(exist).build();
    }

    @Consumes("application/json")
    @Produces("application/json")
    @PATCH
    @Path("/{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long heroId, @Valid HeroRequest HeroRequest) {
        Hero exist = heroRepository.findById(heroId);

        if (exist == null) return Response.status(Status.NOT_FOUND).build();

        if (HeroRequest.getAlias() != null) exist.setAlias(HeroRequest.getAlias());
        if (HeroRequest.getName() != null) exist.setName(HeroRequest.getName());
        if (HeroRequest.getCanFly() != null) exist.setCanFly(HeroRequest.getCanFly());

        return Response.status(Status.OK).entity(exist).build();
    }

}
