package org.ksm;

import java.util.List;

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
    public Response create(Hero hero) {
        hero.id = null;
        hero.persist();
        Response response = Response.status(Status.CREATED).build();
        return response;
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
    public Response replaceById(@PathParam("id") Long heroId, Hero hero) {
        Hero exist = heroRepository.findById(heroId);
        
        if (exist == null) return Response.status(Status.NOT_FOUND).build();
        
        exist.alias = hero.alias;
        exist.name = hero.name;
        exist.canFly = hero.canFly;
        return Response.status(Status.OK).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long heroId, Hero hero) {
        Hero exist = heroRepository.findById(heroId);

        if (exist == null) return Response.status(Status.NOT_FOUND).build();

        if (hero.alias != null) exist.alias = hero.alias;
        if (hero.name != null) exist.name = hero.name;
        if (hero.canFly != null) exist.canFly = hero.canFly;
        return Response.status(Status.OK).build();
    }

}
