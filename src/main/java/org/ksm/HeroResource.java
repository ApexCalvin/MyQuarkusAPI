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
@Consumes("application/json")
@Produces("application/json")
public class HeroResource {
    
    @Inject
    HeroRepository heroRepository;

    @GET
    @Path("/load")
    @Transactional
    public void load(){
        new Hero("batman", "bruce", false).persist(); 
        new Hero("ironman", "tony", true).persist();
    }

    @GET
    @Operation(summary = "test summary for getAll", description = "test description for getAll")
    public List<Hero> getAll() {
         return heroRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Hero getById(@PathParam("id") Long heroId) {
        return heroRepository.findById(heroId);
    }

    @POST
    @Transactional
    public Response create(@Valid Hero hero) {
        hero.id = null;
        hero.persist();
        return Response.status(Status.CREATED).entity(hero).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long heroId) {
        if (heroRepository.deleteById(heroId)) {
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

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
